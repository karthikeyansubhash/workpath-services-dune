import os
import re
import csv
import argparse
import xml.etree.ElementTree as ET
from datetime import datetime

def parse_api_list_from_md(md_file_path):
    """Parse workpath-api-list.md to get the list of APIs that should be tested
    
    Returns:
        tuple: (api_list_full, api_class_method_map)
            - api_list_full: Dictionary of (package, class, method) -> True
            - api_class_method_map: Dictionary of (class, method) -> package (for fallback lookup)
    """
    api_list_full = {}
    api_class_method_map = {}
    
    if not os.path.exists(md_file_path):
        print(f"Warning: API list file not found: {md_file_path}")
        return api_list_full, api_class_method_map
    
    with open(md_file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    # Parse markdown table
    in_table = False
    for line in lines:
        line = line.strip()
        
        # Check if we're in the table
        if line.startswith('|') and 'API Package Name' in line:
            in_table = True
            continue
        
        # Skip separator line
        if in_table and line.startswith('|---'):
            continue
        
        # Parse table rows
        if in_table and line.startswith('|'):
            parts = [p.strip() for p in line.split('|')]
            if len(parts) >= 4:  # | package | class | method |
                package = parts[1]
                class_name = parts[2]
                method = parts[3]
                
                if package and class_name and method:
                    full_key = (package, class_name, method)
                    api_list_full[full_key] = True
                    
                    # Also store (class, method) -> package mapping for fallback
                    short_key = (class_name, method)
                    api_class_method_map[short_key] = package
        
        # Stop if we reach another section
        if in_table and line.startswith('#'):
            break
    
    print(f"Loaded {len(api_list_full)} APIs from {md_file_path}")
    return api_list_full, api_class_method_map

def parse_xml_test_results(xml_file_path):
    """Parse JUnit XML test results
    
    Returns:
        List of test results with structure matching HTML parser output
    """
    test_results = []
    
    try:
        tree = ET.parse(xml_file_path)
        root = tree.getroot()
        
        for testcase in root.findall('testcase'):
            test_name = testcase.get('name', '')
            classname = testcase.get('classname', '')
            time_str = testcase.get('time', '')
            
            failure = testcase.find('failure')
            error = testcase.find('error')
            skipped = testcase.find('skipped')
            
            if failure is not None or error is not None:
                status = 'failed'
            elif skipped is not None:
                status = 'skipped'
            else:
                status = 'passed'
            
            test_results.append({
                'class': classname,
                'test': test_name,
                'status': status,
                'duration': time_str
            })
    
    except Exception as e:
        print(f"Error parsing XML file {xml_file_path}: {e}")
    
    return test_results

def parse_package_and_class(full_class_name):
    """Parse package name and class name from full class name
    
    Converts: com.hp.workpath.apitest.xxx -> com.hp.workpath.api.xxx
    """
    parts = full_class_name.rsplit('.', 1)
    if len(parts) == 2:
        package = parts[0]
        class_name = parts[1]
        # Convert apitest to api
        package = package.replace('.apitest.', '.api.')
        return package, class_name
    return '', full_class_name

def extract_api_class_name(test_name):
    """Extract API Class Name from test method name
    
    Naming Convention: [ClassName]_[Method1]_[Method2]_..._[Scenario]
    Example: AccessoryService_open_getinfo_close_ReturnsResultOK
    
    Returns:
        API Class Name (first part before _) or empty string if no _ found
    """
    if '_' in test_name:
        parts = test_name.split('_')
        return parts[0]
    return ''

def extract_methods_list(test_name):
    """Extract API methods as a list
    
    Handles parameterized tests by removing bracket parameters first.
    
    Format: [ClassName]_[Method1]_[Method2]_..._$[Scenario]
    
    Rules:
    - If '$' exists: methods are between ClassName and $
    - If no '$': methods are between ClassName and last part (Scenario)
    
    Examples:
    - DeviceService_getString_$ReturnValue[0: DA_NETWORK_HOSTNAME]
      → methods: ['getString']
    - AccessoryService_open_close_$GivenDeviceReady_WhenOpen_ThenSuccess
      → methods: ['open', 'close']
    - AccessoryService_open_getinfo_close_ReturnsOK (no $, legacy format)
      → methods: ['open', 'getinfo', 'close']
    
    Returns:
        List of method names (excludes class name and scenario)
    """
    # Remove parameterized test parameters [...]
    clean_name = re.sub(r'\[.*?\]', '', test_name)
    
    # Handle $ separator (both _$ and $ formats)
    if '$' in clean_name:
        method_part = clean_name.split('$')[0]
        method_part = method_part.rstrip('_')  # Remove trailing _ from _$
        parts = method_part.split('_')
        
        if len(parts) >= 2:
            # parts[0] is ClassName, parts[1:] are methods
            return parts[1:]
    
    # No $ separator: use traditional parsing
    if '_' in clean_name:
        parts = clean_name.split('_')
        if len(parts) > 2:
            # parts[0] = ClassName
            # parts[1:-1] = API Methods
            # parts[-1] = Scenario/ExpectedResult
            return parts[1:-1]
    
    # If no _ found, return empty list
    return []

def map_to_api_structure(test_results, api_list_md_path, separate_methods=True):
    """Map test results to API structure matching the image format
    
    Only shows APIs defined in workpath-api-list.md
    
    Naming Convention: [ClassName]_[Method1]_[Method2]_[Method3]_[Scenario]
    Example: AccessoryService_open_getinfo_close_ReturnsResultOK
    
    Groups test cases by API method - one row per API method with multiple TCs listed
    Also checks against API list to find methods with no test cases
    
    Args:
        test_results: List of test results
        api_list_md_path: Path to workpath-api-list.md file
        separate_methods: If True, create separate row for each API method
    """
    # Load API list that should be tested
    expected_apis_full, api_class_method_map = parse_api_list_from_md(api_list_md_path)
    
    # Dictionary to group TCs by (package, class, method) - only for expected APIs
    method_groups = {}
    
    for result in test_results:
        # Parse API Class Name from test method name (first part before _)
        api_class_name = extract_api_class_name(result['test'])
        
        # Fallback to original class name if no _ found
        if not api_class_name:
            package, api_class_name = parse_package_and_class(result['class'])
        else:
            # Use full package from original class
            package, _ = parse_package_and_class(result['class'])
        
        # Extract methods as list using _ delimiter
        methods_list = extract_methods_list(result['test'])
        
        if separate_methods and len(methods_list) > 1:
            # Create separate row for each method
            for method in methods_list:
                # Try to match with full package first
                key = (package, api_class_name, method)
                actual_package = package
                
                # If not found, try fallback with (class, method) lookup
                if key not in expected_apis_full:
                    short_key = (api_class_name, method)
                    if short_key in api_class_method_map:
                        actual_package = api_class_method_map[short_key]
                        key = (actual_package, api_class_name, method)
                
                # Only add if this API is in the expected list
                if key in expected_apis_full:
                    if key not in method_groups:
                        method_groups[key] = []
                    method_groups[key].append({
                        'test_name': result['test'],
                        'status': result['status']
                    })
        else:
            # Single method or no methods
            if methods_list:
                for method in methods_list:
                    # Try to match with full package first
                    key = (package, api_class_name, method)
                    actual_package = package
                    
                    # If not found, try fallback with (class, method) lookup
                    if key not in expected_apis_full:
                        short_key = (api_class_name, method)
                        if short_key in api_class_method_map:
                            actual_package = api_class_method_map[short_key]
                            key = (actual_package, api_class_name, method)
                    
                    # Only add if this API is in the expected list
                    if key in expected_apis_full:
                        if key not in method_groups:
                            method_groups[key] = []
                        method_groups[key].append({
                            'test_name': result['test'],
                            'status': result['status']
                        })
    
    # Convert grouped data to list - start with tested APIs
    api_data = []
    tested_apis = set(method_groups.keys())
    
    for (package, class_name, method), test_cases in method_groups.items():
        # Collect all TC names and statuses
        tc_list = []
        statuses = []
        
        for tc in test_cases:
            status = tc['status']
            statuses.append(status)
            
            # Format TC name with status
            if status == 'failed':
                tc_formatted = f'<span style="color: red; font-weight: bold;">{tc["test_name"]} - Failed</span>'
            else:
                tc_formatted = tc['test_name']
            
            tc_list.append(tc_formatted)
        
        # Determine overall status (if any failed, mark as failed)
        overall_status = 'failed' if 'failed' in statuses else 'passed'
        
        api_data.append({
            'package': package,
            'class': class_name,
            'methods': method,
            'tc_count': len(test_cases),
            'tc_names': '<br>'.join(tc_list),
            'status': overall_status
        })
    
    # Add APIs with no test cases (only from expected list)
    for (package, class_name, method) in expected_apis_full.keys():
        key = (package, class_name, method)
        if key not in tested_apis:
            api_data.append({
                'package': package,
                'class': class_name,
                'methods': method,
                'tc_count': 0,
                'tc_names': '<span style="color: orange; font-style: italic;">No test cases found</span>',
                'status': 'no_tc'
            })
    
    return api_data

def generate_html_report(api_data, output_file, version='Unknown'):
    """Generate HTML report with table
    
    Args:
        api_data: List of API test data
        output_file: Output HTML file path
        version: Version string from version.properties
    """
    
    # Calculate statistics for summary
    total_apis = len(api_data)
    tested_apis = sum(1 for x in api_data if x['status'] != 'no_tc')
    passed_apis = sum(1 for x in api_data if x['status'] == 'passed')
    failed_apis = sum(1 for x in api_data if x['status'] == 'failed')
    no_tc_apis = sum(1 for x in api_data if x['status'] == 'no_tc')
    
    total_tcs = sum(x['tc_count'] for x in api_data)
    passed_tcs = sum(x['tc_count'] for x in api_data if x['status'] == 'passed')
    failed_tcs = sum(x['tc_count'] for x in api_data if x['status'] == 'failed')
    
    report_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    
    html_content = f"""<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Workpath API Test Report</title>
    <style>
        body {{
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }}
        .container {{
            max-width: 1800px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }}
        h1 {{
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }}
        table {{
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }}
        th {{
            background-color: #d3d3d3;
            color: #333;
            font-weight: bold;
            padding: 12px;
            text-align: left;
            border: 1px solid #ccc;
            position: sticky;
            top: 0;
            z-index: 10;
        }}
        td {{
            padding: 10px;
            border: 1px solid #ccc;
            vertical-align: top;
        }}
        tr:nth-child(even) {{
            background-color: #f9f9f9;
        }}
        tr:hover {{
            background-color: #f0f0f0;
        }}
        tr.no-tc-row {{
            background-color: #fff3e0 !important;
        }}
        tr.no-tc-row:hover {{
            background-color: #ffe0b2 !important;
        }}
        .passed {{
            color: green;
            font-weight: bold;
        }}
        .failed {{
            color: red;
            font-weight: bold;
        }}
        .no_tc {{
            color: orange;
            font-weight: bold;
        }}
        .skipped {{
            color: orange;
            font-weight: bold;
        }}
        .summary {{
            margin-bottom: 20px;
            padding: 15px;
            background-color: #e8f4f8;
            border-radius: 5px;
        }}
        .summary-section {{
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }}
        .summary-section:last-child {{
            border-bottom: none;
            margin-bottom: 0;
            padding-bottom: 0;
        }}
        .summary-title {{
            font-size: 14px;
            font-weight: bold;
            color: #2c5aa0;
            margin-bottom: 8px;
            text-transform: uppercase;
        }}
        .summary-item {{
            display: inline-block;
            margin-right: 30px;
            font-size: 16px;
        }}
        .summary-label {{
            font-weight: bold;
            color: #555;
        }}
        .filter-container {{
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
        }}
        .filter-buttons {{
            display: flex;
            gap: 10px;
        }}
        .btn-filter {{
            padding: 6px 14px;
            background-color: #f0f0f0;
            color: #333;
            border: 2px solid #ccc;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 500;
            transition: all 0.2s;
        }}
        .btn-filter:hover {{
            background-color: #e0e0e0;
        }}
        .btn-filter.active {{
            background-color: #2c5aa0;
            color: white;
            border-color: #2c5aa0;
        }}
        .filter-container input {{
            padding: 8px;
            width: 300px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }}
        .btn-export {{
            padding: 8px 16px;
            background-color: #2c5aa0;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
        }}
        .btn-export:hover {{
            background-color: #1e4278;
        }}
        .col-package {{
            width: 20%;
        }}
        .col-class {{
            width: 15%;
        }}
        .col-method {{
            width: 12%;
        }}
        .col-count {{
            width: 8%;
        }}
        .col-status {{
            width: 8%;
        }}
        .col-testname {{
            width: 37%;
        }}
    </style>
    <script>
        function filterTable() {{
            const input = document.getElementById('searchInput');
            const filter = input.value.toUpperCase();
            const table = document.getElementById('resultTable');
            const tr = table.getElementsByTagName('tr');
            
            for (let i = 1; i < tr.length; i++) {{
                const td = tr[i].getElementsByTagName('td');
                let found = false;
                
                for (let j = 0; j < td.length; j++) {{
                    if (td[j]) {{
                        const txtValue = td[j].textContent || td[j].innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {{
                            found = true;
                            break;
                        }}
                    }}
                }}
                
                // Check status filter
                const statusFilter = document.querySelector('.btn-filter.active')?.dataset.status || 'all';
                const statusCell = td[4]; // Status column
                const statusText = statusCell ? statusCell.textContent.trim().toUpperCase() : '';
                
                let statusMatch = true;
                if (statusFilter !== 'all') {{
                    statusMatch = statusText === statusFilter.toUpperCase();
                }}
                
                tr[i].style.display = (found && statusMatch) ? '' : 'none';
            }}
        }}
        
        function filterByStatus(status) {{
            // Update active button
            document.querySelectorAll('.btn-filter').forEach(btn => {{
                btn.classList.remove('active');
            }});
            event.target.classList.add('active');
            
            // Apply filter
            filterTable();
        }}
        
        function sortTable(columnIndex) {{
            const table = document.getElementById('resultTable');
            let switching = true;
            let shouldSwitch, i;
            let switchcount = 0;
            let dir = 'asc';
            
            while (switching) {{
                switching = false;
                const rows = table.rows;
                
                for (i = 1; i < (rows.length - 1); i++) {{
                    shouldSwitch = false;
                    const x = rows[i].getElementsByTagName('TD')[columnIndex];
                    const y = rows[i + 1].getElementsByTagName('TD')[columnIndex];
                    
                    if (dir == 'asc') {{
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {{
                            shouldSwitch = true;
                            break;
                        }}
                    }} else if (dir == 'desc') {{
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {{
                            shouldSwitch = true;
                            break;
                        }}
                    }}
                }}
                
                if (shouldSwitch) {{
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    switchcount++;
                }} else {{
                    if (switchcount == 0 && dir == 'asc') {{
                        dir = 'desc';
                        switching = true;
                    }}
                }}
            }}
        }}
        
        function exportToCSV() {{
            const table = document.getElementById('resultTable');
            const rows = table.querySelectorAll('tr');
            let csvContent = '';
            
            rows.forEach(row => {{
                const cols = row.querySelectorAll('th, td');
                const rowData = [];
                
                cols.forEach(col => {{
                    // Remove HTML tags and get text content
                    let text = col.textContent.trim();
                    // Escape quotes and wrap in quotes if contains comma
                    text = text.replace(/"/g, '""');
                    if (text.includes(',') || text.includes('\\n') || text.includes('"')) {{
                        text = '"' + text + '"';
                    }}
                    rowData.push(text);
                }});
                
                csvContent += rowData.join(',') + '\\n';
            }});
            
            // Create download link
            const blob = new Blob([csvContent], {{ type: 'text/csv;charset=utf-8;' }});
            const link = document.createElement('a');
            const url = URL.createObjectURL(blob);
            link.setAttribute('href', url);
            link.setAttribute('download', 'API_Test_Results_' + new Date().toISOString().split('T')[0] + '.csv');
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }}
    </script>
</head>
<body>
    <div class="container">
        <h1>Workpath API Test Report</h1>
        
        <div class="summary">
            <!-- API Statistics -->
            <div class="summary-section">
                <div class="summary-title">API Statistics</div>
                <div class="summary-item">
                    <span class="summary-label">Total APIs:</span> {total_apis}
                </div>
                <div class="summary-item">
                    <span class="summary-label">Tested APIs:</span> {tested_apis}
                </div>
                <div class="summary-item">
                    <span class="summary-label">Passed APIs:</span> <span class="passed">{passed_apis}</span>
                </div>
                <div class="summary-item">
                    <span class="summary-label">Failed APIs:</span> <span class="failed">{failed_apis}</span>
                </div>
                <div class="summary-item">
                    <span class="summary-label">No TC APIs:</span> <span class="no_tc">{no_tc_apis}</span>
                </div>
            </div>
            
            <!-- Test Case Statistics -->
            <div class="summary-section">
                <div class="summary-title">Test Case Statistics</div>
                <div class="summary-item">
                    <span class="summary-label">Total TCs:</span> {total_tcs}
                </div>
                <div class="summary-item">
                    <span class="summary-label">Passed TCs:</span> <span class="passed">{passed_tcs}</span>
                </div>
                <div class="summary-item">
                    <span class="summary-label">Failed TCs:</span> <span class="failed">{failed_tcs}</span>
                </div>
            </div>
            
            <!-- Report Info -->
            <div class="summary-section">
                <div class="summary-item">
                    <span class="summary-label">Version:</span> {version}
                </div>
                <div class="summary-item">
                    <span class="summary-label">Generated:</span> {report_time}
                </div>
            </div>
        </div>
        
        <div class="filter-container">
            <div class="filter-buttons">
                <button class="btn-filter active" data-status="all" onclick="filterByStatus('all')">All</button>
                <button class="btn-filter" data-status="passed" onclick="filterByStatus('passed')">Passed</button>
                <button class="btn-filter" data-status="failed" onclick="filterByStatus('failed')">Failed</button>
                <button class="btn-filter" data-status="no tc" onclick="filterByStatus('no tc')">No TC</button>
            </div>
            <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Search for package, class, method, or test name...">
            <button class="btn-export" onclick="exportToCSV()">Export to CSV</button>
        </div>
        
        <table id="resultTable">
            <thead>
                <tr>
                    <th class="col-package" onclick="sortTable(0)" style="cursor: pointer;">API Package ▼</th>
                    <th class="col-class" onclick="sortTable(1)" style="cursor: pointer;">API Class ▼</th>
                    <th class="col-method" onclick="sortTable(2)" style="cursor: pointer;">API Method ▼</th>
                    <th class="col-count" onclick="sortTable(3)" style="cursor: pointer;">TC Count ▼</th>
                    <th class="col-status" onclick="sortTable(4)" style="cursor: pointer;">Status ▼</th>
                    <th class="col-testname" onclick="sortTable(5)" style="cursor: pointer;">TC Name ▼</th>
                </tr>
            </thead>
            <tbody>
"""
    
    # Add table rows
    for data in api_data:
        status_class = data['status'].lower()
        status_text = 'NO TC' if status_class == 'no_tc' else data['status'].upper()
        row_class = 'no-tc-row' if status_class == 'no_tc' else ''
        
        html_content += f"""                <tr class="{row_class}">
                    <td>{data['package']}</td>
                    <td>{data['class']}</td>
                    <td>{data['methods']}</td>
                    <td style="text-align: center;">{data['tc_count']}</td>
                    <td class="{status_class}">{status_text}</td>
                    <td>{data['tc_names']}</td>
                </tr>
"""
    
    html_content += """            </tbody>
        </table>
    </div>
</body>
</html>
"""
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(html_content)
    
    print(f"HTML report created: {output_file}")

def generate_csv_report(api_data, output_file):
    """Generate CSV report"""
    
    with open(output_file, 'w', encoding='utf-8-sig', newline='') as f:
        writer = csv.writer(f)
        
        # Write header
        writer.writerow(['API Package', 'API Class', 'API Method', 'TC Count', 'Status', 'TC Name'])
        
        # Write data rows
        for data in api_data:
            status_text = 'NO TC' if data['status'] == 'no_tc' else data['status'].upper()
            
            # Remove HTML tags from tc_names
            tc_names = data['tc_names']
            tc_names = re.sub(r'<br>', ' | ', tc_names)
            tc_names = re.sub(r'<[^>]+>', '', tc_names)
            
            writer.writerow([
                data['package'],
                data['class'],
                data['methods'].replace('<br>', ' | '),
                data['tc_count'],
                status_text,
                tc_names
            ])
    
    print(f"CSV report created: {output_file}")

def main():
    parser = argparse.ArgumentParser(
        description='Generate HTML and CSV reports from JUnit XML test results',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Use default XML file path
  python generate_html_report_from_xml.py
  
  # Specify XML file
  python generate_html_report_from_xml.py --xml-file path/to/test-results.xml
  
  # Specify all parameters
  python generate_html_report_from_xml.py --xml-file test.xml --api-list workpath-api-list.md --output-dir ./reports
        """
    )
    
    parser.add_argument(
        '--xml-file',
        type=str,
        default='./tests/api/apiTest/build/outputs/androidTest-results/connected/debug/TEST-result.xml',
        help='Path to JUnit XML test results file (default: ./tests/api/apiTest/build/outputs/androidTest-results/connected/debug/TEST-result.xml)'
    )
    
    parser.add_argument(
        '--api-list',
        type=str,
        default='./tests/api/apiTest/docs/workpath-api-list.md',
        help='Path to API list markdown file (default: ./tests/api/apiTest/docs/workpath-api-list.md)'
    )
    
    parser.add_argument(
        '--output-dir',
        type=str,
        default='./api-test-report',
        help='Output directory for HTML and CSV reports (default: ./api-test-report)'
    )
    
    parser.add_argument(
        '--no-csv',
        action='store_true',
        help='Skip CSV report generation (only generate HTML report)'
    )
    
    parser.add_argument(
        '--version',
        type=str,
        default='Unknown',
        help='Version string to display in the report (default: Unknown)'
    )
    
    args = parser.parse_args()
    
    # Validate XML file exists
    if not os.path.exists(args.xml_file):
        print(f"Error: XML file not found: {args.xml_file}")
        return 1
    
    # Validate API list file exists
    if not os.path.exists(args.api_list):
        print(f"Error: API list file not found: {args.api_list}")
        return 1
    
    # Create output directory if it doesn't exist
    os.makedirs(args.output_dir, exist_ok=True)
    
    print(f"Parsing XML test results from: {args.xml_file}")
    
    # Parse XML test results
    all_results = parse_xml_test_results(args.xml_file)
    
    print(f"\nTotal test cases found: {len(all_results)}")
    
    # Map to API structure
    api_data = map_to_api_structure(all_results, args.api_list, separate_methods=True)
    
    # Create HTML report
    output_file = os.path.join(args.output_dir, "API_Test_Results.html")
    generate_html_report(api_data, output_file, args.version)
    
    # Create CSV report (optional)
    if not args.no_csv:
        csv_file = os.path.join(args.output_dir, "API_Test_Results.csv")
        generate_csv_report(api_data, csv_file)
        print(f"\nDone! Found {len(api_data)} API methods")
        print(f"HTML output: {output_file}")
        print(f"CSV output: {csv_file}")
    else:
        print(f"\nDone! Found {len(api_data)} API methods")
        print(f"HTML output: {output_file}")
    
    return 0

if __name__ == "__main__":
    exit(main())

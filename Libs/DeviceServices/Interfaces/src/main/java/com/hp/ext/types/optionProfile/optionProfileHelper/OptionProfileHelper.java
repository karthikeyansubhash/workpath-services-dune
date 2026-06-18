package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.hp.ext.types.optionProfile.AndOperator;
import com.hp.ext.types.optionProfile.ConditionOperator;
import com.hp.ext.types.optionProfile.EqualsOperator;
import com.hp.ext.types.optionProfile.GreaterThanOperator;
import com.hp.ext.types.optionProfile.LessThanOperator;
import com.hp.ext.types.optionProfile.NotEqualsOperator;
import com.hp.ext.types.optionProfile.NotOperator;
import com.hp.ext.types.optionProfile.OptionDefinition;
import com.hp.ext.types.optionProfile.OptionRule;
import com.hp.ext.types.optionProfile.OrOperator;
import com.hp.ext.types.optionProfile.PossibleValuesRule;
import com.hp.ext.types.optionProfile.RangeRule;
import com.hp.ext.types.optionProfile.RegularExpressionRule;
import com.hp.ext.types.optionProfile.StringLengthRule;
import com.hp.ext.types.optionProfile.ValidValuesRule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OptionProfileHelper<TOptions> {
    private ConditionOperatorCache conditionOperatorCache = new ConditionOperatorCache();
    private Map<String, OptionDefinition> optionDefinitions = null;
    private Map<String, Object> optionProperties = new HashMap<String, Object>();
    private Map<String, List<String>> allPossibleValuesMap = new HashMap<String, List<String>>();
    private Map<String, List<String>> currentAvailableValuesMap = new HashMap<String, List<String>>();

    private TOptions optionsInstance;

    /**
     * Accessor to the options instance used at construction time
     */
    public TOptions getOptionsInstance() {
        return optionsInstance;
    }

    public void setOptionsInstance(TOptions value) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        this.optionsInstance = value;
        evaluate();
    }

    private List<OptionRuleNotification> currentOptionRuleNotifications = new ArrayList<OptionRuleNotification>();

    public List<OptionRuleNotification> getCurrentOptionRuleNotifications() {
        return currentOptionRuleNotifications;
    }


    /**
     * The default constructor
     *
     * @param optionsInstance   An instance of the options that will be evaulated by
     *                          default
     * @param optionDefinitions A map of OptionDefinitions that apply to the
     *                          instance type. The key is expected to be the
     *                          OptionName value of the OptionDefinition converted
     *                          to lowercase.
     * @param type              The class of the instance options.
     */
    public OptionProfileHelper(TOptions optionsInstance, Map<String, OptionDefinition> optionDefinitions,
                               Class<TOptions> type)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.optionsInstance = optionsInstance;

        this.optionDefinitions = (optionDefinitions != null) ? optionDefinitions : new HashMap<String, OptionDefinition>();

        initialize(type);
    }

    private void initialize(Class<TOptions> type)
            throws ClassNotFoundException, NoSuchFieldException, SecurityException, NoSuchMethodException,
            OptionProfileHelperException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Field[] localProperties = type.getDeclaredFields();

        for (Field f : localProperties) {

            // Let's normalize the property-name to lowercase
            String propertyName = f.getName().toLowerCase();

            if ("this$0".equals(propertyName))
                continue;

            // Add the PropertyInfo instance to our OptionProperties map
            optionProperties.put(propertyName, f);
            Type propertyType = f.getType();
            Class<?> typeClass = Class.forName(propertyType.getTypeName());
            //
            // Now create the corresponding entry into the AllPossibleValues map. We use
            // this
            // map as the superset when calculating the current available values durning an
            // Evaluate operation...
            //

            if (OptionTypeHelper.isE2EnumerationType(propertyType)) {
                // It's an E2-style enumeration...
                List<String> allEnumValues = OptionTypeHelper.getE2Enumerations(propertyType);
                allPossibleValuesMap.put(propertyName, allEnumValues);
            } else if (typeClass.isEnum()) {
                // It's a proper C# enumerationr
                Object[] possibleValues = typeClass.getEnumConstants();
                List<String> allEnumValues = new ArrayList<String>();
                for (Object value : possibleValues) {
                    allEnumValues.add(value.toString());
                }
                allPossibleValuesMap.put(propertyName, allEnumValues);
            } else if (OptionTypeHelper.isE2BindablePolymorph(propertyType)) {
                // We special-case support for a Bindable Polymorph
                allPossibleValuesMap.put(propertyName, null);
                List<List<String>> filter = new ArrayList<List<String>>();
                filter.add(Arrays.asList("IsExplicit", "IsExpression", "Explicit", "Expression"));
                filter.add(Arrays.asList("ExplicitValue", "ExpressionPattern"));
                addClassMembers(propertyType, propertyName + ".", 0, filter);
            }
            // If not an Enum, Interface, or primitive, it is a class
            else if (!typeClass.isEnum() && !typeClass.isInterface() && !typeClass.isPrimitive()
                    && !"java.lang.String".equals(typeClass.getName())) {
                // For now, we don't do anything special, but in the future we may have
                // additional
                // special-case needs for certain object types as properties in the TOptions
                // type.
                allPossibleValuesMap.put(propertyName, null);
            } else {
                allPossibleValuesMap.put(propertyName, null);
            }

            // //
            // // And now we initialize the CurrentAvailableValues map for this option
            // //
            currentAvailableValuesMap.put(propertyName, null);
        }
        // Lastly, we evaluate
        evaluate();
    }

    private void addClassMembers(Type propertyType, String prefix, int depth, List<List<String>> filter)
            throws ClassNotFoundException {

        if (null == filter || depth >= filter.size()) {
            // That's it, we're done.
            return;
        }

        String className;
        if ("boolean".equals(propertyType.getTypeName())) {
            className = "java.lang.Boolean";
        } else {
            className = propertyType.getTypeName();
        }
        Class<?> typeClass = Class.forName(className);

        int localDepth = depth + 1;
        List<String> localFilter = (null == filter.get(depth) ? new ArrayList<String>() : filter.get(depth));

        Method[] localProperties = typeClass.getDeclaredMethods();
        for (Method f : localProperties) {
            // Let's normalize the property-name to lowercase
            String propertyName = f.getName().toLowerCase();

            // Remove the beginning get since polymorph classes work different in java
            propertyName = propertyName.replaceFirst("^get", "");
            if (localFilter.stream().anyMatch(propertyName::equalsIgnoreCase)) {
                // Add the PropertyInfo instance to our OptionProperties map
                optionProperties.put(prefix + propertyName, f);

                //
                // Now create the corresponding entry into the AllPossibleValues map. We use
                // this
                // map as the superset when calculating the current available values durning an
                // Evaluate operation...
                //

                Type innerPropertyType = f.getReturnType();
                Class<?> innerTypeClass = Class.forName(propertyType.getTypeName());
                if (OptionTypeHelper.isE2EnumerationType(propertyType)) {
                    // It's an E2-style enumeration...
                    List<String> allEnumValues = OptionTypeHelper.getE2Enumerations(propertyType);
                    allPossibleValuesMap.put(prefix + propertyName, allEnumValues);
                } else if (typeClass.isEnum()) {
                    // It's a proper C# enumerationr
                    Object[] possibleValues = typeClass.getEnumConstants();
                    List<String> allEnumValues = new ArrayList<String>();
                    for (Object value : possibleValues) {
                        allEnumValues.add(value.toString());
                    }
                    allPossibleValuesMap.put(propertyName, allEnumValues);
                }
                // If not an Enum, Interface, or primitive, it is a class
                else if (!typeClass.isEnum() && !typeClass.isInterface() && !typeClass.isPrimitive()
                        && !"java.lang.String".equals(typeClass.getName())) {
                    allPossibleValuesMap.put(prefix + propertyName, null);
                    addClassMembers(innerPropertyType, prefix + propertyName + ".", localDepth, filter);
                } else {
                    allPossibleValuesMap.put(prefix + propertyName, null);
                }

                //
                // And now we initialize the CurrentAvailableValues map for this option
                //
                currentAvailableValuesMap.put(prefix + propertyName, null);
            }
        }
    }

    public Object getOptionPropertyInfo(String key) {
        return optionProperties.get(key.toLowerCase());
    }

    public OptionDefinition getOptionDefinition(String key) {
        return optionDefinitions.get(key.toLowerCase());
    }

    private Object getOptionPropertyValue(String property, TOptions optionsInstance)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object obj = optionsInstance;
        if (optionsInstance != null) {
            for (String p : property.split("\\.")) {
                if (obj == null) {
                    break;
                }
                List<Method> methods = Arrays.stream(obj.getClass().getDeclaredMethods())
                        .filter(f -> Modifier.isPublic(f.getModifiers())).collect(Collectors.toList());
                Method info = null;
                for (Method m : methods) {
                    // Looking for either getter or bool
                    if (m.getName().equalsIgnoreCase("get" + p) || m.getName().equalsIgnoreCase(p)) {
                        info = m;
                        break;
                    }
                }

                if (info == null) {
                    obj = null;
                    break;
                }

                obj = info.invoke(obj);
            }
        }

        // //
        // // We have the property-value, but if the value is an E2Enumeration or
        // E2Alias, we want to
        // // fetch the "underlying" value.
        // // TODO - E2Polymorph?
        // //

        if (null != obj) {
            if (obj.getClass().getSuperclass().getName().equalsIgnoreCase("com.hp.ext.types.common.E2Enumeration")
                    || obj.getClass().getSuperclass().getName().equalsIgnoreCase("com.hp.ext.types.common.E2Alias")) {
                List<Method> methods = Arrays.stream(obj.getClass().getSuperclass().getDeclaredMethods())
                        .filter(f -> Modifier.isPublic(f.getModifiers())).collect(Collectors.toList());
                Method info = null;
                for (Method m : methods) {
                    if (m.getName().equalsIgnoreCase("getValue")) {
                        info = m;
                        break;
                    }
                }
                if (null != info) {
                    obj = info.invoke(obj);
                }

            }
        }

        return obj;
    }

    /**
     * Calculates all possible values, using the unconditional PossibleValues rules
     *
     * @param optionName The option whose values are being
     *                   calculated
     * @return Returns null if ALL values are possible, empty-list if NO values
     * are possible, and the list of possible values otherwise
     */
    private List<String> calculatePossibleValues(String optionName) {
        List<OptionRule> possibleValuesRules = null;
        List<String> possibleValues = null;

        // Fetch the OptionDefinition
        OptionDefinition definition = getOptionDefinition(optionName);

        // Normalize optionName to lowercase to use as key into our map
        String optionMapKey = optionName.toLowerCase();

        if (null == definition) {
            // If there is NO OptionDefinition at all, then we must default to what
            // is found in the AllPossibleValuesMap
            possibleValues = allPossibleValuesMap.get(optionMapKey);
        } else {
            // We need to inspect the OptionRules in the definition...

            ;
            List<OptionRule> definitionRules = definition.getRules() == null ? new ArrayList<OptionRule>()
                    : definition.getRules();

            if (null == definitionRules.stream().filter(o -> o.isPossibleValues()).findFirst().orElse(null)) {
                // Since there are no explicit PossibleValue rules at all then again we
                // must default to what is found in the AllPossibleValuesMap
                possibleValues = allPossibleValuesMap.get(optionMapKey);
            } else {
                //
                // This means there was one-or-more PossibleValues rules, although there should
                // never
                // be more than one as the ValidValues rule should be used to provide
                // qualified/conditional
                // values...
                // So get the PossibleValues rules for processing. Also note, we don't care
                // about conditional
                // because by convention PossibleValues rules will be unconditional...
                //
                possibleValuesRules = definitionRules.stream().filter(o -> o.isPossibleValues())
                        .collect(Collectors.toList());

                if (0 == possibleValuesRules.size()) {
                    // If there are no PossibleValues rules at all, then we
                    // allow whatever is found in the AllPossibleValuesMap
                    possibleValues = allPossibleValuesMap.get(optionMapKey);
                } else {
                    // Loop over PossibleValues rules and build up the current set
                    for (OptionRule optionRule : possibleValuesRules) {
                        PossibleValuesRule possibleValuesRule = optionRule.getPossibleValues();
                        if (null != possibleValuesRule.getOptionValues()) {
                            if (possibleValues == null) {
                                possibleValues = possibleValuesRule.getOptionValues();
                            } else {
                                possibleValues = possibleValues.stream().distinct()
                                        .filter(possibleValuesRule.getOptionValues()::contains)
                                        .collect(Collectors.toList());
                            }
                        }
                    }
                }
            }
        }

        return possibleValues;

    }

    /**
     * Helper method to calculate current available values for an option
     *
     * @param optionName      The option name to calculate
     * @param optionsInstance The optionsInstance to use when evaluating a condition
     * @return Returns null if ALL values are available, empty-list if NO values are
     * available, list of current available values otherwise
     */
    private List<String> calculateCurrentAvailableValues(String optionName, TOptions optionsInstance)
            throws OptionProfileHelperException, IllegalArgumentException, IllegalAccessException {
        List<OptionRule> validValuesRules = null;
        List<String> currentValidValues = null;
        List<String> currentPossibleValues = null;
        Object pi = null;

        // Fetch the pi to confirm the property exists
        pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        // Fetch the OptionDefinition for this property
        OptionDefinition definition = getOptionDefinition(optionName);

        // Normalize optionName to lowercase to use as key into our map
        String mapKey = optionName.toLowerCase();

        if (null == definition) {
            // If there is no OptionDefinition at all, then we must default to what
            // is found in the AllPossibleValuesMap
            currentPossibleValues = allPossibleValuesMap.get(mapKey);
        } else {
            //
            // Ok, lets use the calculatePossibleValues method to get the values that were
            // either
            // determined at initialization or defined with a PossibleValues rule
            //
            currentPossibleValues = calculatePossibleValues(optionName);

            //
            // Next we need to inspect the OptionRules in the definition...
            //

            List<OptionRule> definitionRules = definition.getRules() == null ? new ArrayList<OptionRule>()
                    : definition.getRules();

            if (null != definitionRules.stream().filter(o -> o.isValidValues()).findFirst().orElse(null)) {
                //
                // This means there was one-or-more ValidValues. So we need to fetch those that
                // are currently
                // active as defined by evaluating any conditions
                //
                validValuesRules = definitionRules.stream().filter(o -> {
                    try {
                        return o.isValidValues() && (o.getValidValues().getCondition() == null
                                || evaluateCondition(o.getValidValues().getCondition(), optionsInstance));
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());

                if (validValuesRules.size() > 0) {
                    // Loop over all ValidValuesRules and build up the current set
                    for (OptionRule rule : validValuesRules) {
                        ValidValuesRule validValuesRule = rule.getValidValues();

                        if (null != validValuesRule.getOptionValues()) {
                            if (currentValidValues == null) {
                                currentValidValues = validValuesRule.getOptionValues();
                            } else {
                                currentValidValues = currentValidValues.stream().distinct()
                                        .filter(validValuesRule.getOptionValues().toString()::contains)
                                        .collect(Collectors.toList());
                            }
                        }
                    }

                    // Normalize...
                    currentValidValues = (null != currentValidValues) ? currentValidValues : new ArrayList<String>();
                }
            }
        }

        if (null == currentValidValues) {
            // If we end up with null for currentValidValues, then what is available
            // is what we got for currentPossibleValues...
            return currentPossibleValues;
        } else {
            // Otherwise, what is availabe is the currentValidValues.
            return currentValidValues;
        }
    }

    /**
     * Evaluates a ConditionOperator using the OptionsInstance
     *
     * @param conditionOperator The ConditionOperator to evaluate
     * @param optionsInstance   The optionsInstance to use when evaluating a
     *                          condition
     * @return Returns true if the condition is met, otherwise false
     */
    public Boolean evaluateConditionOperator(ConditionOperator conditionOperator, TOptions optionsInstance)
            throws InvocationTargetException, IllegalArgumentException, IllegalAccessException {
        Boolean result = false;

        if (null != conditionOperator) {
            if (true == ConditionOperatorExtensions.isEqualsOperator(conditionOperator)) {
                EqualsOperator equals = ConditionOperatorExtensions.asEqualsOperator(conditionOperator,
                        conditionOperatorCache);
                // Fetch the current value
                Object currentValue = getOptionPropertyValue(equals.getOptionName(), optionsInstance);

                String value = currentValue == null ? "null" : currentValue.toString();

                // Compare as string (case-insensitive) TODO - will we ever need case-sensitive?
                result = equals.getOptionValue().equalsIgnoreCase(value);
            } else if (true == ConditionOperatorExtensions.isNotEqualsOperator(conditionOperator)) {
                NotEqualsOperator notEquals = ConditionOperatorExtensions.asNotEqualsOperator(conditionOperator,
                        conditionOperatorCache);

                // Fetch the current value
                Object currentValue = getOptionPropertyValue(notEquals.getOptionName(), optionsInstance);

                String value = currentValue == null ? "null" : currentValue.toString();

                // Compare as string (case-insensitive) TODO - will we ever need case-sensitive?
                result = !notEquals.getOptionValue().equalsIgnoreCase(value);
            } else if (true == ConditionOperatorExtensions.isLessThanOperator(conditionOperator)) {
                LessThanOperator lessThan = ConditionOperatorExtensions.asLessThanOperator(conditionOperator,
                        conditionOperatorCache);

                // Fetch the option instance current value
                Object currentValue = getOptionPropertyValue(lessThan.getOptionName(), optionsInstance);
                String currentValueString = currentValue == null ? "null" : currentValue.toString();

                // Compare
                Long currentDecimalValue = null;
                Long lessThanDecimalValue = null;

                if (!"null".equals(currentValueString) && lessThan.getOptionValue() != null) {
                    currentDecimalValue = Long.parseLong(currentValueString);
                    lessThanDecimalValue = Long.parseLong(lessThan.getOptionValue());

                    result = currentDecimalValue < lessThanDecimalValue;
                }
            } else if (true == ConditionOperatorExtensions.isGreaterThanOperator(conditionOperator)) {
                GreaterThanOperator greaterThan = ConditionOperatorExtensions.asGreaterThanOperator(conditionOperator,
                        conditionOperatorCache);

                // Fetch the option instance current value
                Object currentValue = getOptionPropertyValue(greaterThan.getOptionName(), optionsInstance);
                String currentValueString = currentValue == null ? "null" : currentValue.toString();

                // Compare
                Long currentDecimalValue = null;
                Long greaterThanDecimalValue = null;
                if (!"null".equals(currentValueString) && greaterThan.getOptionValue() != null) {
                    currentDecimalValue = Long.parseLong(currentValueString);
                    greaterThanDecimalValue = Long.parseLong(greaterThan.getOptionValue());

                    result = currentDecimalValue > greaterThanDecimalValue;
                }
            }
        }

        return result;
    }

    /**
     * Evaluates a ConditionOperator using the OptionsInstance
     *
     * @param conditionOperator The Condition to evaluate
     * @param optionsInstance   The optionsInstance to use when evaluating the
     *                          condition
     * @return Returns true if the condition is met, otherwise false
     */
    public Boolean evaluateCondition(ConditionOperator conditionOperator, TOptions optionsInstance)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Boolean result = false;

        if (null != conditionOperator) {
            if (true == ConditionOperatorExtensions.isAndOperator(conditionOperator)) {
                AndOperator and = ConditionOperatorExtensions.asAndOperator(conditionOperator, conditionOperatorCache);

                Boolean andResult = true;

                for (ConditionOperator op : and.getOperators()) {
                    if (false == evaluateConditionOperator(op, optionsInstance)) {
                        // Since one is false, the complete AND is false
                        andResult = false;
                        break;
                    }
                }

                result = andResult;
            } else if (ConditionOperatorExtensions.isOrOperator(conditionOperator)) {
                OrOperator or = ConditionOperatorExtensions.asOrOperator(conditionOperator, conditionOperatorCache);

                Boolean orResult = false;

                for (ConditionOperator op : or.getOperators()) {
                    if (true == evaluateConditionOperator(op, optionsInstance)) {
                        // Since one is true, the complete OR is true
                        orResult = true;
                        break;
                    }
                }

                result = orResult;
            } else if (ConditionOperatorExtensions.isNotOperator(conditionOperator)) {
                NotOperator not = ConditionOperatorExtensions.asNotOperator(conditionOperator, conditionOperatorCache);

                Boolean notResult = evaluateCondition(not.getOperator(), optionsInstance);

                result = !(notResult);

            } else {
                // just evaluate the single operator
                result = evaluateConditionOperator(conditionOperator, optionsInstance);
            }
        }

        return result;
    }

    /**
     * Evaluate a given Range rule using the OptionsInstance
     *
     * @param optionName      The option name to which the Range rule
     * @param rangeRule       The Range rule to evaluate
     * @param optionsInstance The optionsInstance to use when evaluating the
     *                        rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluateRangeRule(String optionName, RangeRule rangeRule, TOptions optionsInstance)
            throws OptionProfileHelperException {
        OptionRuleNotification notification = null;
        try {
            // Fetch the current value, which should be numeric
            Object currentValue = getOptionPropertyValue(optionName, optionsInstance);
            if (currentValue != null) {
                String stringToConvert = String.valueOf(currentValue);
                Long currentValueAsDecimal;

                if (stringToConvert.equals("null")) {
                    currentValueAsDecimal = 0L;
                } else {
                    currentValueAsDecimal = Long.parseLong(stringToConvert);
                }

                if (currentValueAsDecimal < rangeRule.getLowerBoundary().getValue() ||
                        currentValueAsDecimal > rangeRule.getUpperBoundary().getValue()) {
                    // Conflict!
                    notification = new OptionRuleConflictNotification(optionName, OptionRuleConflictType.Range,
                            new OptionRule(rangeRule));
                }
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("evaluateRangeRule", e);
        }

        return notification;
    }

    /**
     * Evaluate a given StringLength rule using the OptionsInstance
     *
     * @param optionName       The option name to which the StringLength rule
     * @param stringLengthRule The StringLength rule to evaluate
     * @param optionsInstance  The optionsInstance to use when evaluating the
     *                         rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluateStringLengthRule(String optionName, StringLengthRule stringLengthRule,
                                                           TOptions optionsInstance) throws OptionProfileHelperException {
        OptionRuleNotification notification = null;
        try {
            // Fetch the current value, which should be a string
            Object currentValue = getOptionPropertyValue(optionName, optionsInstance);
            String currentValueAsString = String.valueOf(currentValue);
            currentValueAsString = currentValueAsString.equals("null") ? "" : currentValueAsString;

            if (currentValueAsString.length() < stringLengthRule.getMinimumLength().getValue() ||
                    currentValueAsString.length() > stringLengthRule.getMaximumLength().getValue()) {
                // Conflict!
                notification = new OptionRuleConflictNotification(optionName, OptionRuleConflictType.StringLength,
                        new OptionRule(stringLengthRule));
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("evaluateStringLengthRule", e);
        }

        return notification;
    }

    /**
     * Evaluate a given RegularExpression rule using the OptionsInstance
     *
     * @param optionName            The option name to which the RegularExpression
     *                              rule applies
     * @param regularExpressionRule The RegularExpression rule to evaluate
     * @param optionsInstance       The optionsInstance to use when evaluating the
     *                              rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluateRegularExpressionRule(String optionName,
                                                                RegularExpressionRule regularExpressionRule, TOptions optionsInstance) throws OptionProfileHelperException {
        OptionRuleNotification notification = null;
        try {
            // Fetch the current value, which should be a string
            Object currentValue = getOptionPropertyValue(optionName, optionsInstance);
            String currentValueAsString = String.valueOf(currentValue);

            currentValueAsString = currentValueAsString.equals("null") ? "" : currentValueAsString;

            Pattern p = Pattern.compile(regularExpressionRule.getExpression());
            Matcher m = p.matcher(currentValueAsString);
            if (!m.matches()) {
                // Conflict!
                notification = new OptionRuleConflictNotification(optionName, OptionRuleConflictType.RegularExpression,
                        new OptionRule(regularExpressionRule));
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("evaluateRegularExpressionRule", e);
        }

        return notification;
    }

    /**
     * Evaluate a given ValidValues rule using the OptionsInstance
     *
     * @param optionName      The option name to which the ValidValues
     *                        rule applies
     * @param validValuesRule The ValidValues rule to evaluate
     * @param optionsInstance The optionsInstance to use when evaluating the
     *                        rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluateValidValuesRule(String optionName, ValidValuesRule validValuesRule,
                                                          TOptions optionsInstance) throws OptionProfileHelperException {
        OptionRuleNotification notification = null;

        try {
            // Fetch the current value
            Object currentValue = getOptionPropertyValue(optionName, optionsInstance);
            currentValue = (currentValue != null) ? currentValue : "null";

            if (!"null".equals(currentValue) && false == validValuesRule.getOptionValues().stream()
                    .anyMatch(currentValue.toString()::equalsIgnoreCase)) {
                // Conflict!
                notification = new OptionRuleConflictNotification(optionName, OptionRuleConflictType.ValidValue,
                        new OptionRule(validValuesRule));
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("evaluateValidValuesRule", e);
        }

        return notification;
    }

    /**
     * Evaluate a given PossibleValues rule using the OptionsInstance
     *
     * @param optionName         The option name to which the PossibleValues
     *                           rule applies
     * @param possibleValuesRule The PossibleValues rule to evaluate
     * @param optionsInstance    The optionsInstance to use when evaluating the
     *                           rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluatePossibleValuesRule(String optionName, PossibleValuesRule possibleValuesRule,
                                                             TOptions optionsInstance) throws OptionProfileHelperException {
        OptionRuleNotification notification = null;

        try {
            // Fetch the current value
            Object currentValue = getOptionPropertyValue(optionName, optionsInstance);

            currentValue = (currentValue != null) ? currentValue : "null";

            if (!"null".equals(currentValue) && false == possibleValuesRule.getOptionValues().stream()
                    .anyMatch(currentValue.toString()::equalsIgnoreCase)) {
                // Conflict!
                notification = new OptionRuleConflictNotification(optionName, OptionRuleConflictType.ValidValue,
                        new OptionRule(possibleValuesRule));
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("evaluatePossibleValuesRule", e);
        }

        return notification;
    }

    /**
     * Evaluates and calculates the Available Values for a given option
     *
     * @param optionName      The option name
     * @param optionsInstance The optionsInstance to use when evaluating the rule
     * @return Returns an OptionRuleConflictNotification or null
     */
    public OptionRuleNotification evaluateAvailableValues(String optionName, TOptions optionsInstance)
            throws OptionProfileHelperException, IllegalArgumentException, IllegalAccessException {
        OptionRuleNotification notification = null;
        List<String> evaluatedAvailableValues;
        List<String> currentAvailableValues;
        Object pi;

        // Fetch the pi to confirm the property exists
        pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        // Normalize the name to use in our option map(s)
        String optionMapKey = optionName.toLowerCase();

        // Fetch the last last known available values for the option
        currentAvailableValues = currentAvailableValuesMap.get(optionMapKey);

        // Calculate current available values for the option
        evaluatedAvailableValues = calculateCurrentAvailableValues(optionName, optionsInstance);

        if (evaluatedAvailableValues == null && currentAvailableValues == null) {
            // No change...
        } else if ((currentAvailableValues != null && evaluatedAvailableValues == null) ||
                (currentAvailableValues == null && evaluatedAvailableValues != null)) {
            // There has been a change!
            notification = new OptionRuleAvailableValuesNotification(optionName, evaluatedAvailableValues, null);
        } else {
            // We need to compare the lists to look for a change...
            if (false == currentAvailableValues.equals(evaluatedAvailableValues)) {
                // Change!
                notification = new OptionRuleAvailableValuesNotification(optionName, evaluatedAvailableValues, null);
            }
        }

        // Update the CurrentAvailableValues map
        currentAvailableValuesMap.put(optionMapKey, evaluatedAvailableValues);

        return notification;
    }

    public List<OptionRuleNotification> evaluate() throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        return evaluate(optionsInstance, true);
    }

    /**
     * Evaluate the provided TOptions instance with all OptionDefinitions
     *
     * @param optionsInstance The instance to evaluate
     * @param isStateful      A flag to instruct the helper to track any
     *                        notifications and use them for future comparisons when
     *                        evaluating changes to AvailableValues.
     * @return The list of OptionRuleNotifications triggered during evaluation. Will
     * NOT include OptionRuleAvailableValuesNotification if the isStateful
     * flag is false.
     */
    public List<OptionRuleNotification> evaluate(TOptions optionsInstance, Boolean isStateful)
            throws OptionProfileHelperException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        OptionRuleNotification notification = null;
        List<OptionRuleNotification> notifications = new ArrayList<OptionRuleNotification>();

        if (null == optionsInstance) {
            throw new OptionProfileHelperException("Cannot evaluate a null optionsInstance");
        }

        for (var entry : optionDefinitions.entrySet()) {
            OptionDefinition definition = entry.getValue();

            if (null == getOptionPropertyInfo(definition.getOptionName())) {
                // This OptionDefinition is for a property that doesn't exist on this Option object!
                // Intentionally skipping this definition. The instance doesn't match the definition.
                continue;
            }

            if (false == definition.getIsAvailable()) {
                // This option is not available according to profile, so let's just ignore it
                continue;
            }

            // Normalize the definition for ease of use below...
            if (null == definition.getRules()) {
                definition.setRules(new ArrayList<OptionRule>());
            }

            // If multiple valid values rules are present then only one needs to pass. This requires a separate evaluation
            if (definition.getRules().stream().filter(OptionRule::isValidValues).count() > 1) {
                List<OptionRule> validValuesRules = definition.getRules().stream().filter(OptionRule::isValidValues).collect(Collectors.toList());
                Map<Integer, OptionRuleNotification> validValuesResults = new HashMap<>();
                for (int i = 0; i < validValuesRules.size(); i++) {
                    // Create dictionary containing the results of evaluating each valid values rule
                    if (validValuesRules.get(i).getValidValues().getCondition() == null || evaluateCondition(validValuesRules.get(i).getValidValues().getCondition(), optionsInstance)) {
                        validValuesResults.put(i, evaluateValidValuesRule(definition.getOptionName(), validValuesRules.get(i).getValidValues(), optionsInstance));
                    }
                }

                // If there isn't a null value within the dictionary, then we know that all valid values check have failed.
                if (!validValuesResults.containsValue(null)) {
                    for (OptionRuleNotification result : validValuesResults.values()) {
                        if (result != null) {
                            notifications.add(result);
                        }
                    }
                }
            }

            for (OptionRule rule : definition.getRules()) {
                notification = null;

                if (rule.isDisable()) {
                    if (rule.getDisable().getCondition() == null
                            || true == evaluateCondition(rule.getDisable().getCondition(), optionsInstance)) {
                        // Nothing the actually evaluate...
                        notification = new OptionRuleDisabledNotification(definition.getOptionName(), rule);
                    }
                } else if (rule.isForceSet()) {
                    if (rule.getForceSet().getCondition() == null
                            || true == evaluateCondition(rule.getForceSet().getCondition(), optionsInstance)) {
                        // Nothing the actually evaluate
                        notification = new OptionRuleForceSetNotification(definition.getOptionName(),
                                rule.getForceSet().getOptionValue(), rule.getForceSet());
                    }
                } else if (rule.isRange()) {
                    if (rule.getRange().getCondition() == null
                            || true == evaluateCondition(rule.getRange().getCondition(), optionsInstance)) {
                        notification = evaluateRangeRule(definition.getOptionName(), rule.getRange(), optionsInstance);
                    }
                } else if (rule.isStringLength()) {
                    if (rule.getStringLength().getCondition() == null
                            || true == evaluateCondition(rule.getStringLength().getCondition(), optionsInstance)) {
                        notification = evaluateStringLengthRule(definition.getOptionName(), rule.getStringLength(),
                                optionsInstance);
                    }
                } else if (rule.isRegularExpression()) {
                    if (rule.getRegularExpression().getCondition() == null
                            || true == evaluateCondition(rule.getRegularExpression().getCondition(), optionsInstance)) {
                        notification = evaluateRegularExpressionRule(definition.getOptionName(),
                                rule.getRegularExpression(), optionsInstance);
                    }
                } else if (rule.isValidValues()) {
                    if (definition.getRules().stream().filter(OptionRule::isValidValues).count() <= 1) {
                        if (rule.getValidValues().getCondition() == null
                                || true == evaluateCondition(rule.getValidValues().getCondition(), optionsInstance)) {
                            notification = evaluateValidValuesRule(definition.getOptionName(), rule.getValidValues(),
                                    optionsInstance);
                        }
                    }
                } else if (rule.isPossibleValues()) {
                    if (rule.getPossibleValues().getCondition() == null
                            || true == evaluateCondition(rule.getPossibleValues().getCondition(), optionsInstance)) {
                        notification = evaluatePossibleValuesRule(definition.getOptionName(), rule.getPossibleValues(),
                                optionsInstance);
                    }
                }

                if (null != notification) {
                    notifications.add(notification);
                }
            }
        }

        if (isStateful) {
            // Evaluate AvailableValues
            for (Map.Entry<String, Object> entry : optionProperties.entrySet()) {
                notification = evaluateAvailableValues(entry.getKey(), optionsInstance);
                if (null != notification) {
                    notifications.add(notification);
                }
            }

            // Stash the notifications
            currentOptionRuleNotifications = notifications;
        }

        return notifications;
    }

    /**
     * Get the possible value set (as strings) for a given option
     *
     * @param optionName The name of the option property
     * @return Returns null if ALL values are possible, empty-list if NO values are
     * possible, list of possible values otherwise
     * @implNote Use this method to get an unconditional list of all possible values
     * for the option
     */
    public List<String> getPossibleValues(String optionName) throws OptionProfileHelperException {
        List<String> possibleValues;
        Object pi;

        // Fetch the pi to confirm the property exists
        pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        possibleValues = calculatePossibleValues(optionName);

        // Null means *ALL* values are possible
        // Empty means *NO* values are possible
        // Non-empty means *THOSE* values are possible
        return possibleValues;
    }

    /**
     * Get the possible value set for a given option
     *
     * @param optionName The name of the option property
     * @param classT     The class of the option property
     * @return Returns null if ALL values are possible, empty-list if NO values are
     * possible, list of current possible values otherwise
     * @implNote Use this method to get an unconditional list of all possible values
     * for the option
     */
    public <T> List<T> getPossibleValues(String optionName, Class<T> classT) throws OptionProfileHelperException {
        List<T> result = null;

        List<String> possibleValues = getPossibleValues(optionName);

        // Null means *ALL* values are possible
        // Empty means *NO* values are possible
        // Non-empty means *THOSE* values are possible
        if (null != possibleValues) {
            result = OptionTypeHelper.castValues(possibleValues, classT);
        }

        return result;
    }

    /// <summary>
    /// Get the current available value set (as strings) for a given option
    /// </summary>
    /// <param name="optionName">The name of the option property</param>
    /// <returns>Returns null if ALL values are available, empty-list if NO values
    /// are available, list of current available values otherwise</returns>


    /**
     * Get the current available value set (as strings) for a given option
     *
     * @param optionName      The name of the option property
     * @param optionsInstance The instance to evaluate. If set to null will use the
     *                        default instance
     * @return Returns null if ALL values are available, empty-list if NO values are
     * available, list of current available values otherwise
     */
    public List<String> getCurrentAvailableValues(String optionName, TOptions optionsInstance)
            throws OptionProfileHelperException, IllegalArgumentException, IllegalAccessException {
        List<String> currentAvailableValues;
        Object pi;

        // Fetch the pi to confirm the property exists
        pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        var evaluationInstance = (null == optionsInstance ? this.optionsInstance : optionsInstance);

        currentAvailableValues = calculateCurrentAvailableValues(optionName, evaluationInstance);

        // Null means *ALL* values are possible
        // Empty means *NO* values are possible
        // Non-empty means *THOSE* values are possible
        return currentAvailableValues;
    }

    /**
     * Get the current available value set for a given option
     *
     * @param optionName      The name of the option property
     * @param optionsInstance The instance to evaluate. If set to null will use the
     *                        default instance
     * @param classT          The class of the option property
     * @return Returns null if ALL values are available, empty-list if NO values are
     * available, list of current available values otherwise
     */
    public <T> List<T> getCurrentAvailableValues(String optionName, TOptions optionsInstance, Class<T> classT)
            throws OptionProfileHelperException, IllegalArgumentException, IllegalAccessException {
        List<T> result = null;
        List<String> currentAvailableValues;
        Object pi;

        // Fetch the pi to confirm the property exists
        pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        if (((Field) pi).getType() != classT) {
            throw new OptionProfileHelperException("Property " + optionName + " is not type " + classT);
        }

        var evaluationInstance = (null == optionsInstance ? this.optionsInstance : optionsInstance);

        currentAvailableValues = calculateCurrentAvailableValues(optionName, evaluationInstance);

        // Null means *ALL* values are possible
        // Empty means *NO* values are possible
        // Non-empty means *THOSE* values are possible
        if (null != currentAvailableValues) {
            result = OptionTypeHelper.castValues(currentAvailableValues, classT);
        }

        return result;
    }

    /**
     * Get any current/active Range rule that might be applied to the given option
     *
     * @param optionName      The name of the option to get the current Range rule
     *                        from
     * @param optionsInstance The instance to evaluate. If set to null will use the
     *                        default instance
     * @return Returns the first current/active Range rule found, or null
     */
    public RangeRule getCurrentRange(String optionName, TOptions optionsInstance) throws OptionProfileHelperException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        RangeRule currentRange = null;

        // Fetch the pi to confirm the property exists
        Object pi = getOptionPropertyInfo(optionName);
        if (null == pi) {
            throw new OptionProfileHelperException("No Option property with name " + optionName);
        }

        var evaluationInstance = (null == optionsInstance ? this.optionsInstance : optionsInstance);

        OptionDefinition definition = getOptionDefinition(optionName);

        if (null != definition) {
            // Normalize the definition for ease of use below...
            if (null == definition.getRules()) {
                definition.setRules(new ArrayList<OptionRule>());
            }

            for (OptionRule rule : definition.getRules()) {
                if (rule.isRange()) {
                    if (true == (rule.getRange().getCondition() == null
                            || evaluateCondition(rule.getRange().getCondition(), evaluationInstance))) {
                        // This Range has it's Condition met, so we treat it as CurrentRange
                        currentRange = rule.getRange();
                        break;
                    }
                }
            }
        }

        return currentRange;
    }

    /**
     * Get the set of options that currently are in conflict (invalid)
     *
     * @return Returns the list of conflicting options, or empty list if there are no conflicts
     */
    public List<OptionRuleNotification> getConflicts() {
        return getConflicts(getCurrentOptionRuleNotifications());
    }

    /**
     * Get the set of options from the provided set of notifications that are in
     * conflict (invalid)
     *
     * @param notifications The set of notifications to search through
     * @return Returns the list of conflicting options, or empty list if there are
     * no conflicts
     */
    public List<OptionRuleNotification> getConflicts(List<OptionRuleNotification> notifications) {
        if (null == notifications) {
            return new ArrayList<OptionRuleNotification>();
        }

        return notifications.stream().filter(o -> o.getNotificationType() == OptionRuleNotificationType.OptionConflict)
                .collect(Collectors.toList());
    }

    /**
     * Get the list of option properties that are available as determined by the
     * loaded profile
     *
     * @return The list of available options, empty if no options are available.
     */
    public List<String> getAvailableOptions() {
        List<String> result = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : optionProperties.entrySet()) {

            String propertyName = entry.getKey();

            var definition = getOptionDefinition(propertyName);

            if (null != definition) {
                if (true == definition.getIsAvailable()) {
                    // The OptionDefinition has declared this property available
                    result.add(propertyName);
                }
            } else {
                // By definition, if there is no OptionDefinition for the property, it is
                // available...
                result.add(propertyName);
            }
        }

        return result;
    }

    /**
     * Get the list of option properties that are NOT available as determined by the
     * loaded profile
     *
     * @return The list of un-available options, empty if all options are available
     */
    public List<String> getUnavailableOptions() {
        List<String> result = new ArrayList<String>();

        for (Map.Entry<String, Object> entry : optionProperties.entrySet()) {
            String propertyName = entry.getKey();

            var definition = getOptionDefinition(propertyName);

            if (null != definition) {
                if (false == definition.getIsAvailable()) {
                    // The OptionDefinition has declared this property NOT available
                    result.add(propertyName);
                }
            }
        }

        return result;
    }

    /**
     * A convenience method to extract the disabled options from the
     * CurrentNotifications
     *
     * @return Returns a list of the names of the options disabled, empty if no
     * options are disabled
     */
    public List<String> getDisabledOptions() {
        return getDisabledOptions(getCurrentOptionRuleNotifications());
    }

    /**
     * A convenience method to extract the disabled options from a set of
     * OptionRuleNotifications
     *
     * @param notifications The list of OptionRuleNotifications to check
     * @return Returns a list of the names of the options disabled, empty if no
     * options are disabled
     */
    public List<String> getDisabledOptions(List<OptionRuleNotification> notifications) {
        List<String> result = new ArrayList<String>();

        notifications = null != notifications ? notifications : new ArrayList<OptionRuleNotification>();

        List<OptionRuleNotification> disabledNotifications = notifications.stream()
                .filter(o -> o.getNotificationType() == OptionRuleNotificationType.OptionDisabled)
                .collect(Collectors.toList());
        for (OptionRuleNotification disabled : disabledNotifications) {
            Object propertyInfo = getOptionPropertyInfo(disabled.getOptionName());

            if (null != propertyInfo) {
                result.add(disabled.getOptionName());
            }
        }

        return result;
    }

    /**
     * Check to see if the held OptionsInstance is without conflicts.
     *
     * @param evaluateFirst Set to false to prevent a full evaluation from occurring
     *                      before checking for validity
     * @return Returns true if there are no conflicts, false otherwise. If false,
     * use CurrentNotifications to find the conflicts
     */
    public Boolean isValid(Boolean evaluateFirst) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (evaluateFirst) {
            evaluate(optionsInstance, true);
        }

        List<OptionRuleNotification> conflicts = currentOptionRuleNotifications.stream()
                .filter(o -> o.getNotificationType() == OptionRuleNotificationType.OptionConflict)
                .collect(Collectors.toList());

        return (conflicts.size() == 0);
    }

    /**
     * Check to see if the provided optionsInstance is without conflicts
     *
     * @param optionsInstance The instance to evaluate
     * @return Returns true if there are no conflicts, false otherwise. If false,
     * use CurrentNotifications to find the conflicts
     */
    public Boolean isValid(TOptions optionsInstance) throws OptionProfileHelperException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (null == optionsInstance) {
            throw new OptionProfileHelperException("optionsInstance must not be null");
        }

        // Kick off an evaluation, but let's not make it stateful...
        var notifications = evaluate(optionsInstance, false);

        List<OptionRuleNotification> conflicts = notifications.stream()
                .filter(o -> o.getNotificationType() == OptionRuleNotificationType.OptionConflict)
                .collect(Collectors.toList());

        return (conflicts.size() == 0);
    }

}

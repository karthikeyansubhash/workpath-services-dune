/*
 * Copyright (C) 2013 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ipaulpro.afilechooser;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;

import static com.ipaulpro.afilechooser.utils.FileUtils.PATH;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity that handles the FileListFragments
 *
 * @author paulburke (ipaulpro)
 * @version 2013-06-25
 */
public class FileChooserActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener, FileListFragment.Callbacks {

    public static final String EXTERNAL_BASE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    private FragmentManager mFragmentManager;
    private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, R.string.storage_removed, Toast.LENGTH_LONG).show();
            finishWithResult(null);
        }
    };

    private String mPath;
    private TextView tvPath;
    private ImageButton btnSaveFolder;
    private RelativeLayout layoutFolderNavigation;
    private HorizontalScrollView scrollView;
    private String mode;

    //    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_file_chooser);

        ImageButton ivBack = (ImageButton) findViewById(R.id.iv_back);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean hasBackStack = getFragmentManager().getBackStackEntryCount() > 0;

                if (hasBackStack) {
                    getFragmentManager().popBackStack();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });

        tvPath = findViewById(R.id.tv_path);

        btnSaveFolder = findViewById(R.id.btn_save);
        btnSaveFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra(PATH, mPath));
                finish();
            }
        });

        layoutFolderNavigation = findViewById(R.id.layout_folder_navigation);
        scrollView = findViewById(R.id.scrollView);
        mode = getIntent().getStringExtra(FileUtils.INTENT_SELECT);
        if (mode != null && mode.equals(FileUtils.FILE)) {
            layoutFolderNavigation.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.choose_file));
        } else {
            tvTitle.setText(getString(R.string.select_directory));
        }

        mFragmentManager = getFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            String path = getIntent().getStringExtra(PATH);

            if (!TextUtils.isEmpty(path)) {
                mPath = path;
            } else {
                mPath = EXTERNAL_BASE_PATH;
            }
            addFragment();
        } else {
            mPath = savedInstanceState.getString(PATH);
        }
        setNavigationPath(mPath);
        setTitle(mPath);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterStorageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerStorageListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PATH, mPath);
    }

    @Override
    public void onBackStackChanged() {
        int count = mFragmentManager.getBackStackEntryCount();

        if (count > 0) {
            FragmentManager.BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
        } else {
            mPath = EXTERNAL_BASE_PATH;
        }

        if (tvPath != null) {
            setNavigationPath(mPath);
        }
        setTitle(mPath);
        invalidateOptionsMenu();
    }

    /**
     * Add the initial Fragment with given path.
     */
    private void addFragment() {
        FileListFragment fragment = FileListFragment.newInstance(mPath, mode);
        mFragmentManager.beginTransaction()
                .add(R.id.mainFragmentContainer, fragment).commit();
    }

    /**
     * "Replace" the existing Fragment with a new one using given path. We're
     * really adding a Fragment to the back stack.
     *
     * @param file The file (directory) to display.
     */
    private void replaceFragment(File file) {
        mPath = file.getAbsolutePath();

        FileListFragment fragment = FileListFragment.newInstance(mPath, mode);
        mFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(mPath).commit();
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param file The file selected.
     */
    private void finishWithResult(File file) {
        if (file != null) {
            Uri uri = Uri.fromFile(file);
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Called when the user selects a File
     *
     * @param file The file that was selected
     */
    @Override
    public void onFileSelected(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                if (tvPath != null) {
                    setNavigationPath(file.getAbsolutePath());
                }
                replaceFragment(file);
            } else {
                finishWithResult(file);
            }
        } else {
            Toast.makeText(FileChooserActivity.this, R.string.error_selecting_file,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mStorageListener, filter);
    }

    /**
     * Unregister the external storage BroadcastReceiver.
     */
    private void unregisterStorageListener() {
        unregisterReceiver(mStorageListener);
    }

    @Override
    public void setTitle(final CharSequence title) {
        if (!TextUtils.isEmpty(title) && title.toString().startsWith(EXTERNAL_BASE_PATH)) {
            // Replace start with convenient name
            final String newTitle = title.toString().replace(EXTERNAL_BASE_PATH, getString(R.string.base_path_name));
            super.setTitle(newTitle);
        } else {
            super.setTitle(title);
        }
    }

    private void setNavigationPath(String absolutePath) {
        String path = absolutePath.replaceFirst(EXTERNAL_BASE_PATH, "");
        tvPath.setText(path);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        final FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}

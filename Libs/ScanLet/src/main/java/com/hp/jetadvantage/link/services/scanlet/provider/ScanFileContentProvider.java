// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.scanlet.provider;

import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.core.content.FileProvider;

import java.io.FileNotFoundException;

public class ScanFileContentProvider extends FileProvider {
    public static final Uri SCAN_FILE_CONTENT = new Uri.Builder()
            .scheme("content").authority("com.hp.jetadvantage.link.authority.oxp.scanletcp.fileprovider").appendPath(".tmp").build();

    @Override
    public boolean onCreate() {
//        String modRid = "02ddb6de-2507-44ea-8593-e42ce37837cc";
//        File file = new File(getContext().getFilesDir(), "mods/" + modRid + "/");
//        Uri uri = FileProvider.getUriForFile(getContext(), "com.hp.modmanager", file);
        return true;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        return super.openFile(uri, mode);
    }
}
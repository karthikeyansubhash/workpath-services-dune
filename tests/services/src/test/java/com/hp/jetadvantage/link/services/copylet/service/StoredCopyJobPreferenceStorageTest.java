package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StoredCopyJobPreferenceStorageTest {

    @Mock private Context mockContext;
    @Mock private SharedPreferences mockSharedPreferences;
    @Mock private SharedPreferences.Editor mockEditor;

    @Before
    public void setUp() {
        when(mockContext.getSharedPreferences(eq("stored_copy_job_map"), anyInt()))
                .thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.remove(anyString())).thenReturn(mockEditor);
    }

    // ===== remove tests =====

    @Test
    public void GivenSavedData_WhenRemove_ThenRemoveCalled() {
        StoredCopyJobPreferenceStorage.remove(mockContext, "testKey");

        verify(mockEditor).remove("testKey");
        verify(mockEditor).apply();
    }

    // ===== replaceKey tests =====

    @Test
    public void GivenSavedData_WhenReplaceKey_ThenOldRemovedAndNewSaved() {
        when(mockSharedPreferences.getString(eq("oldKey"), eq((String) null))).thenReturn("storedValue");

        StoredCopyJobPreferenceStorage.replaceKey(mockContext, "oldKey", "newKey");

        verify(mockEditor).remove("oldKey");
        verify(mockEditor).putString("newKey", "storedValue");
        verify(mockEditor).apply();
    }

    @Test
    public void GivenNoData_WhenReplaceKey_ThenNothingHappens() {
        when(mockSharedPreferences.getString(eq("oldKey"), eq((String) null))).thenReturn(null);

        StoredCopyJobPreferenceStorage.replaceKey(mockContext, "oldKey", "newKey");

        verify(mockEditor, never()).remove(anyString());
        verify(mockEditor, never()).putString(anyString(), anyString());
    }

    // ===== getCopyAttributes tests =====

    @Test
    public void GivenNoData_WhenGet_ThenNullReturned() {
        when(mockSharedPreferences.getString(eq("missingKey"), eq((String) null))).thenReturn(null);

        assertNull(StoredCopyJobPreferenceStorage.getCopyAttributes(mockContext, "missingKey"));
    }

    // ===== remove with different keys =====

    @Test
    public void GivenDifferentKeys_WhenRemove_ThenCorrectKeyRemoved() {
        StoredCopyJobPreferenceStorage.remove(mockContext, "scanJobId-12345");

        verify(mockEditor).remove("scanJobId-12345");
        verify(mockEditor).apply();
    }

    // ===== replaceKey atomicity =====

    @Test
    public void GivenSavedData_WhenReplaceKey_ThenAtomicEditApplied() {
        when(mockSharedPreferences.getString(eq("rid-abc"), eq((String) null))).thenReturn("encodedData");

        StoredCopyJobPreferenceStorage.replaceKey(mockContext, "rid-abc", "scanJobId-xyz");

        // Verify single apply() call — atomic operation
        verify(mockEditor).remove("rid-abc");
        verify(mockEditor).putString("scanJobId-xyz", "encodedData");
        verify(mockEditor).apply();
    }

    // ===== Preference file name =====

    @Test
    public void GivenAnyOperation_WhenAccessPrefs_ThenCorrectFileNameUsed() {
        StoredCopyJobPreferenceStorage.remove(mockContext, "anyKey");

        verify(mockContext).getSharedPreferences(eq("stored_copy_job_map"), eq(Context.MODE_PRIVATE));
    }
}

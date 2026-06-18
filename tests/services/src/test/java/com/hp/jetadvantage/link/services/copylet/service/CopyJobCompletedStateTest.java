package com.hp.jetadvantage.link.services.copylet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

import android.text.TextUtils;

import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CopyJobCompletedStateTest {

    private MockedStatic<TextUtils> mockedTextUtils;

    @Before
    public void setUp() {
        mockedTextUtils = Mockito.mockStatic(TextUtils.class);
        mockedTextUtils.when(() -> TextUtils.isEmpty(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            return str == null || str.length() == 0;
        });
        mockedTextUtils.when(() -> TextUtils.isDigitsOnly(any())).thenAnswer(invocation -> {
            CharSequence str = invocation.getArgument(0);
            if (str == null || str.length() == 0) return false;
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) return false;
            }
            return true;
        });
    }

    @After
    public void tearDown() {
        if (mockedTextUtils != null) mockedTextUtils.close();
    }

    // ===== buildCredentials tests =====

    @Test
    public void GivenNullPassword_WhenBuildCredentials_ThenReturnsNull() {
        assertNull(CopyJobCompletedState.buildCredentials(null, JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC));
    }

    @Test
    public void GivenEmptyPassword_WhenBuildCredentials_ThenReturnsNull() {
        assertNull(CopyJobCompletedState.buildCredentials("", JobCredentialsAttributes.PasswordType.NUMERIC));
    }

    @Test
    public void GivenAlphaNumericPassword_WhenBuildCredentials_ThenCredentialsMatchType() {
        JobCredentialsAttributes result = CopyJobCompletedState.buildCredentials(
                "myPass123", JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC);
        assertNotNull(result);
        assertEquals(JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC, result.getStoreJobPasswordType());
        assertEquals("myPass123", result.getStoreJobPassword());
    }

    @Test
    public void GivenNumericPassword_WhenBuildCredentials_ThenCredentialsMatchType() {
        JobCredentialsAttributes result = CopyJobCompletedState.buildCredentials(
                "1234", JobCredentialsAttributes.PasswordType.NUMERIC);
        assertNotNull(result);
        assertEquals(JobCredentialsAttributes.PasswordType.NUMERIC, result.getStoreJobPasswordType());
        assertEquals("1234", result.getStoreJobPassword());
    }

    @Test
    public void GivenNullPasswordType_WhenBuildCredentials_ThenReturnsNull() {
        assertNull(CopyJobCompletedState.buildCredentials("pass", null));
    }
}

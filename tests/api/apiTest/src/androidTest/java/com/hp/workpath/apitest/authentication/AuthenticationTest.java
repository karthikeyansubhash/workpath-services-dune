package com.hp.workpath.apitest.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.access.AccessService;
import com.hp.workpath.api.access.AuthenticationAttributes;
import com.hp.workpath.api.access.Principal;
import com.hp.workpath.api.access.SignInAction;
import com.hp.workpath.api.access.UserOverridesAttributes;
import com.hp.workpath.api.access.UserPreferencesAttributes;
import com.hp.workpath.apitest.R;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {

    private static Context mContext;
    private final DutController mDutController;

    public AuthenticationTest() {
        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void SetUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        mDutController.stopApp(mContext);
    }

    // TODO : Since the test BDL file is not ready yet, I will apply it to the next story (DUNE-251992).
//    @Test
//    public void AuthenticationService_isSupported_ReturnsTrue() {
//        boolean supported = AccessService.isSupported(mContext);
//        assertTrue(supported);
//    }
//
//    @Test
//    public void AuthenticationService_InitiateSignIn_SUCCESS_ReturnsSuccessfully() {
//        Result result = new Result();
//
//        try {
//            // Execute signOut method
//            initiateSignIn();
//
//            AuthenticationAttributes attributes = dummyAuthenticationAttributes();
//            SignInAction dummyAction = new SignInAction(SignInAction.Action.SUCCESS, null);
//
//            signIn(attributes, dummyAction);
//
//            // Verify actual authentication state after logout
//            // Get a new AccessService instance to verify
//            Principal principal = AccessService.getCurrentPrincipal(mContext, result);
//            assertNotNull("Principal object should not be null after signOut", principal);
//
//            boolean isAuthenticated = principal.isAuthenticated();
//            assertTrue("User should be logged In (isAuthenticated() should be true) after signIn()", isAuthenticated);
//
//        } catch (Exception e) {
//            // Handle test failure and print stack trace if an exception occurs
//            fail("AuthenticationService_InitiateSignIn_SUCCESS_ReturnsSuccessfully test failed due to an exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void AuthenticationService_InitiateSignIn_CANCEL_ReturnsSuccessfully() {
//        Result result = new Result();
//
//        try {
//            // Execute signOut method
//            initiateSignIn();
//
//            SignInAction dummyAction = new SignInAction(SignInAction.Action.CANCEL, null);
//
//            signIn(null, dummyAction);
//
//            // Verify actual authentication state after logout
//            // Get a new AccessService instance to verify
//            Principal principal = AccessService.getCurrentPrincipal(mContext, result);
//            assertNotNull("Principal object should not be null after signOut", principal);
//
//            boolean isAuthenticated = principal.isAuthenticated();
//            assertFalse("User should be NOT sign in (isAuthenticated() should be false) after signIn()", isAuthenticated);
//
//        } catch (Exception e) {
//            // Handle test failure and print stack trace if an exception occurs
//            fail("AuthenticationService_InitiateSignIn_CANCEL_ReturnsSuccessfully test failed due to an exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void AuthenticationService_InitiateSignIn_FAIL_ReturnsSuccessfully() {
//        Result result = new Result();
//
//        try {
//            // Execute signOut method
//            initiateSignIn();
//
//            SignInAction dummyAction = new SignInAction(SignInAction.Action.FAIL, null);
//
//            signIn(null, dummyAction);
//
//            // Verify actual authentication state after logout
//            // Get a new AccessService instance to verify
//            Principal principal = AccessService.getCurrentPrincipal(mContext, result);
//            assertNotNull("Principal object should not be null after signOut", principal);
//
//            boolean isAuthenticated = principal.isAuthenticated();
//            assertFalse("User should be NOT sign in (isAuthenticated() should be false) after signIn()", isAuthenticated);
//
//        } catch (Exception e) {
//            // Handle test failure and print stack trace if an exception occurs
//            fail("AuthenticationService_InitiateSignIn_CANCEL_ReturnsSuccessfully test failed due to an exception: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void AuthenticationService_SignOut_ReturnsSuccessfully() {
//        Result result = new Result();
//
//        try {
//            // Execute signOut method
//            AccessService.signOut(mContext, result);
//
//            // 1. Verify signOut call result
//            assertEquals("AccessService.signOut() call should return success in Result object", Result.RESULT_OK, result.getCode());
//
//            // Wait for 3 seconds after signOut
//            try {
//                Thread.sleep(3000); // 3000 milliseconds = 3 seconds
//            } catch (InterruptedException e) {
//                // Handle interruption of Thread.sleep()
//                Thread.currentThread().interrupt(); // Restore interruption status
//            }
//
//            // 2. Verify actual authentication state after logout
//            // Get a new AccessService instance to verify
//            Principal principal = AccessService.getCurrentPrincipal(mContext, result);
//            assertNotNull("Principal object should not be null after signOut", principal);
//
//            boolean isAuthenticated = principal.isAuthenticated();
//            assertFalse("User should be logged out (isAuthenticated() should be false) after signOut()", isAuthenticated);
//
//        } catch (Exception e) {
//            // Handle test failure and print stack trace if an exception occurs
//            fail("AuthenticationService_SignOut_ReturnsSuccessfully test failed due to an exception: " + e.getMessage());
//        }
//    }

    private void initiateSignIn() {
        Result result = new Result();

        try {
            // Execute signOut method
            AccessService.initiateSignIn(mContext, result);
            assertEquals("AccessService.initiateSignIn() call should return success in Result object", Result.RESULT_OK, result.getCode());

            // Wait for 3 seconds after signOut
            try {
                Thread.sleep(3000); // 3000 milliseconds = 3 seconds
            } catch (InterruptedException e) {
                // Handle interruption of Thread.sleep()
                Thread.currentThread().interrupt(); // Restore interruption status
            }

        } catch (Exception e) {
            // Handle test failure and print stack trace if an exception occurs
            fail("AuthenticationService initiateSignIn test failed due to an exception: " + e.getMessage());
        }
    }

    private void signIn(AuthenticationAttributes attributes, SignInAction dummyAction) {
        Result result = new Result();

        try {
            AccessService.signIn(mContext, dummyAction, attributes, result);
            assertEquals("AccessService.signIn() call should return success in Result object", Result.RESULT_OK, result.getCode());

            try {
                Thread.sleep(3000); // 3000 milliseconds = 3 seconds
            } catch (InterruptedException e) {
                // Handle interruption of Thread.sleep()
                Thread.currentThread().interrupt(); // Restore interruption status
            }
        } catch (Exception e) {
            // Handle test failure and print stack trace if an exception occurs
            fail("AuthenticationService signIn test failed due to an exception: " + e.getMessage());
        }
    }

    private AuthenticationAttributes dummyAuthenticationAttributes() throws Exception {
        String id = "Tester";
        String password = "password";

        UserOverridesAttributes userOverridesAttributes = new UserOverridesAttributes.Builder()
                .addBccAddress(mContext.getString(R.string.bcc_address_email_01), mContext.getString(R.string.bcc_address_name_01))
                .addBccAddress(mContext.getString(R.string.bcc_address_email_02), mContext.getString(R.string.bcc_address_name_02))
                .addCcAddress(mContext.getString(R.string.cc_address_email_01), mContext.getString(R.string.cc_address_name_01))
                .addCcAddress(mContext.getString(R.string.cc_address_email_02), mContext.getString(R.string.cc_address_name_02))
                .setFrom(mContext.getString(R.string.from_address_email), mContext.getString(R.string.from_address_name))
                .addToAddress(mContext.getString(R.string.to_address_email_01), mContext.getString(R.string.to_address_name_01))
                .addToAddress(mContext.getString(R.string.to_address_email_02), mContext.getString(R.string.to_address_name_02))
                .setMessage(mContext.getString(R.string.email_message))
                .setSubject(mContext.getString(R.string.email_subject))
                .setFaxBillingCode(mContext.getString(R.string.fax_billing_code))
                .setFaxCompanyName(mContext.getString(R.string.fax_company_name))
                .build();

        UserPreferencesAttributes userPreferencesAttributes = new UserPreferencesAttributes.Builder()
                .setAutoLaunchAppAccessPointId(mContext.getString(R.string.app_access_point_id))
                .setLanguageCode(mContext.getString(R.string.language_code))
                .build();

        return new AuthenticationAttributes.WindowsBuilder()
                .setFullyQualifiedName(mContext.getString(R.string.value_fully_qualified_name, id))
                .setDisplayName(id)
                .setPassword(password)
                .setUserDomain(mContext.getString(R.string.value_domain))
                .setUserEmail(mContext.getString(R.string.value_user_email, id))
                .setUserName(id)
                .setUserPrincipalName(id)
                .setHomeFolderPath(mContext.getString(R.string.value_home_folder_path))
                .addUserProperty(mContext.getString(R.string.value_user_property_key_01), mContext.getString(R.string.value_user_property_value_01))
                .addUserProperty(mContext.getString(R.string.value_user_property_key_02), mContext.getString(R.string.value_user_property_value_02))
                .setUserOverridesAttributes(userOverridesAttributes)
                .setUserPreferencesAttributes(userPreferencesAttributes)
                .build();
    }
}

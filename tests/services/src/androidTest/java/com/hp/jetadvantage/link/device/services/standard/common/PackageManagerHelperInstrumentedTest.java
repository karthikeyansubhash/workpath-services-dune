package com.hp.jetadvantage.link.device.services.standard.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInstrumentedTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PackageManagerHelperInstrumentedTest extends StandardDeviceInstrumentedTest {
    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenPackageManagerHelper_WhenGetSolutionIdCalledWithInvalidParam_ThenNullShouldBeReturned() {
        PackageManagerHelper pmHelper = new PackageManagerHelper();
        String solutionId = pmHelper.getSolutionId(null, "");
        assertNull(solutionId);
    }

    //@Test
    /** pre-requisite :
     * pacman apk should be installed, testPackageName should be installed
     */
    public void GivenPackageManagerHelper_WhenGetSolutionIdCalled_ThenSolutionIdShouldBeReturned() {
        String testPackageName = "com.hp.workpath.sample.scansample";
        PackageManagerHelper pmHelper = new PackageManagerHelper();
        String solutionId = pmHelper.getSolutionId(ApplicationProvider.getApplicationContext(), testPackageName);
        assertEquals("11111111-1111-1111-9999-111111111111", solutionId);
    }
}

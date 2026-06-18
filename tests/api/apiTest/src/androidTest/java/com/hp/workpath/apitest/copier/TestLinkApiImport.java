package com.hp.workpath.apitest.copier;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

// Checking if WorkpathApi is accessible
import com.hp.workpath.api.copier.CopierService;

@RunWith(AndroidJUnit4.class)
public class TestLinkApiImport {
    @Test
    public void testImport() {
        System.out.println("CopierService class: " + CopierService.class);
    }
}

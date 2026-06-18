package com.hp.workpath.apitest.massstorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.SsdkUnsupportedException;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.massstorage.CustomerDataFile;
import com.hp.workpath.api.massstorage.MassStorageInfo;
import com.hp.workpath.api.massstorage.MassStorageService;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CustomerDataFileServiceTest {
    private static final String TAG = "CustomerDataFileServiceTest";
    private static Context mContext;
    DutController mDutController;

    // Constructor
    public CustomerDataFileServiceTest() {
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

    public CustomerDataFile getCustomerDataFileFromHDD(String name) {
        Result result = new Result();
        try {
            List<MassStorageInfo> storageList = MassStorageService.getStorageList(mContext, result);
            if(storageList != null) {
                for(MassStorageInfo info : storageList) {
                    if(MassStorageInfo.StorageType.HDD == info.getType()){
                        CustomerDataFile file = new CustomerDataFile(mContext, info, "/"+name);
                        return file;
                    }
                }
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
        return null;
    }

    public CustomerDataFile getCustomerDataFileTypeToUSB() {
        try {
            List<MassStorageInfo> storageList = MassStorageService.getStorageList(mContext, new Result());
            if (storageList != null) {
                CustomerDataFile file = new CustomerDataFile(mContext, storageList.get(0), "test.txt");
                return file;
            }
        } catch(Exception e) {
            fail("getCustomerDataFileTypeToUSB failed:" + e);
        }
        return null;
    }

    @Test
    public void MassStorageService_isSupported_$ReturnsTrue() {
        boolean supported = MassStorageService.isSupported(mContext);
        assertTrue("isSupported", supported);
    }

    @Test
    public void MassStorageService_getStorageList_$GivenUsbConnected_WhenGetStorageList_ThenReturnsUsbType() {
        mDutController.getStorageController().connectUsbDevice();
        Result result = new Result();
        try {
            boolean isUsb = false;
            List<MassStorageInfo> storageList = MassStorageService.getStorageList(mContext, result);
            if(storageList != null){
                for(MassStorageInfo info : storageList) {
                    if(MassStorageInfo.StorageType.USB == info.getType()){
                        isUsb = true;
                    }
                }
            }
            assertTrue(isUsb);
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        } finally {
            mDutController.getStorageController().removeUsbDevice();
        }
    }

    @Test
    public void CustomerDataFile_createNewFile_$GivenHddStorage_WhenCreateNewFile_ThenFileCreated() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                file.createNewFile();
                assertEquals("file path", "/test.txt", file.getPath());
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_exists_$GivenHddStorage_WhenCheckExists_ThenReturnsTrue() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                boolean exist = file.exists();
                assertTrue(exist);
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_getName_$GivenHddStorage_WhenGetName_ThenReturnsFileName() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                String name = file.getName();
                assertEquals("file path", "test.txt", name);
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_isFile_$GivenHddStorage_WhenCheckIsFile_ThenReturnsTrue() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                file.createNewFile();
                boolean isFile = file.isFile();
                assertTrue(isFile);
                file.delete();
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_getPackageName_$GivenHddStorage_WhenGetPackageName_ThenReturnsPackageName() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                String packName = file.getPackageName();
                assertEquals("package name", "com.hp.workpath.apitest", packName);
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_delete_$GivenHddStorageFile_WhenDelete_ThenReturnsTrue() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("test.txt");
            if(file != null) {
                file.createNewFile();
                boolean exist = file.delete();
                assertTrue(exist);
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_mkdir_$GivenHddStorage_WhenMkdir_ThenFolderCreated() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("/testFolder");
            if(file != null) {
                file.mkdir();
                assertEquals("folder path", "/testFolder", file.getPath());
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }

    @Test
    public void CustomerDataFile_delete_$GivenHddStorageFolder_WhenDelete_ThenReturnsTrue() {
        try {
            CustomerDataFile file = getCustomerDataFileFromHDD("testFolder");
            if(file != null) {
                file.mkdir();
                boolean exist = file.delete();
                assertTrue(exist);
            }
        } catch (Exception e) {
            fail("getStorageList failed:" + e);
        }
    }
}

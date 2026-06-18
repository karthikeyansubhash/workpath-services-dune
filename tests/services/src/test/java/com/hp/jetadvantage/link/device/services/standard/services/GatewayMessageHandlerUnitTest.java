package com.hp.jetadvantage.link.device.services.standard.services;

import android.content.Context;

import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GatewayMessageHandlerUnitTest extends TestCase {
    @Mock
    Context mockContext;

    @Mock
    PackageManagerHelper mockPmHelper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        StandardDeviceManagementService.getInstance().getUIContextTokenManager().clearUIConTextToken();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void GivenGatewayMessageHandler_WhenConstructorCalled_ThenObjectCreated() {
        GatewayMessageHandler handler = new GatewayMessageHandler();
        assertNotNull(handler);
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceived_ThenUIContextCached() {

        StandardDeviceManagementService.getInstance().setApplicationContext(mockContext);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String applicationId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String gatewayType = "wgdgtApplication";
        String workpathActionType = "wgdatShowDisplay";
        String uiContextToken = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7ZmFlOThkYTMtNGNjOC00ZDgyLWFmNjItNzExNjA4NjQxODYxfSJ9..fa4V4dbWmHvRqLVx.8uX3ZvRF5Dx3M5Lwr9VYyNJuSRrzSCT78mBKSaiKKQ8Wo7wI-hUslQfC9eZMu_1bLt35cWe89nkUw6Y2ZaKjJtTC9BqjDBh3Oyc_HpfAAumKaOPm4my08CM1Niyo12BP-QPkZpXdkbfz5Fk9fb9jXB_C1WEohYJbzZahdRDYlSnmx5cVTP4-8kUcmviDlcJNJsgEoypOrPElfzY4er3JgoampGYCVGic6RLkKRwsEzAMXRX0HBGjYEvCULpxVRV3hXY4ngqLeBMki8vVxxMq41gLoUsRQCpUZymtbgi_Mi_CAKA_gxy5v8KqT7insS6RDOBMFn8KSG6s6dkV3VrMACgPBSt1HJn3JB3hYOpxPLbRBrSP4wbDnEmbcxBUMrwNmEHYwI6z6WJuRRa7THgjiFZptn5bFL_blQMFFUBFZwRSyiJXzEzKnyV2JF-rp4-CP1bcPHXC76elERuMpGnz_8YsVf-2g9rVAU7z5Z8ZwTkxqlRQ-yfLxyQ-AUpyt6qMFXtT1WgJbFMbqBZo3MCUaDY3gZIetfQL5WlxFfRqtY5q1vX5TZxBmNn6uYY6Wf6NV4xnTxgoH3pNKh9cSZL45mREtn3id6jnyE_yORXjf81QCHxCCQivUvdGMltRmaTmlvc9dWT6ijOTDotVfC67iv5vxBC2yBS3bxDIGg.00bPVvQKhPfURjE-ghBvNg";

        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, makeGatewayMessage(applicationId, solutionId, gatewayType, workpathActionType, uiContextToken));

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertEquals(uiContextToken, token);
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithEmptyUiContext_ThenUIContextNotCached() {

        StandardDeviceManagementService.getInstance().setApplicationContext(mockContext);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String applicationId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String gatewayType = "wgdgtApplication";
        String workpathActionType = "wgdatShowDisplay";
        String uiContextToken = "";

        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, makeGatewayMessage(applicationId, solutionId, gatewayType, workpathActionType, uiContextToken));

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithEmptyAction_ThenUIContextNotCached() {

        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String applicationId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String gatewayType = "application";
        String workpathActionType = "";
        String uiContextToken = "eyJhbGciOiAiZGl..";

        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, makeGatewayMessage(applicationId, solutionId, gatewayType, workpathActionType, uiContextToken));

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithCloseDisplay_ThenUIContextNotCached() {

        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String applicationId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String solutionId = "";
        String gatewayType = "wgdgtApplication";
        String workpathActionType = "wgdatCloseDisplay";
        String uiContextToken = "eyJhbGciOiAiZGl..";

        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, makeGatewayMessage(applicationId, solutionId, gatewayType, workpathActionType, uiContextToken));

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithInvalidValues_ThenUiContextNotCached() {

        StandardDeviceManagementService.getInstance().setApplicationContext(mockContext);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String applicationId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String gatewayType = "invalid";
        String workpathActionType = "invalid";
        String uiContextToken = "eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7ZmFlOThkYTMtNGNjOC00ZDgyLWFmNjItNzExNjA4NjQxODYxfSJ9..fa4V4dbWmHvRqLVx.8uX3ZvRF5Dx3M5Lwr9VYyNJuSRrzSCT78mBKSaiKKQ8Wo7wI-hUslQfC9eZMu_1bLt35cWe89nkUw6Y2ZaKjJtTC9BqjDBh3Oyc_HpfAAumKaOPm4my08CM1Niyo12BP-QPkZpXdkbfz5Fk9fb9jXB_C1WEohYJbzZahdRDYlSnmx5cVTP4-8kUcmviDlcJNJsgEoypOrPElfzY4er3JgoampGYCVGic6RLkKRwsEzAMXRX0HBGjYEvCULpxVRV3hXY4ngqLeBMki8vVxxMq41gLoUsRQCpUZymtbgi_Mi_CAKA_gxy5v8KqT7insS6RDOBMFn8KSG6s6dkV3VrMACgPBSt1HJn3JB3hYOpxPLbRBrSP4wbDnEmbcxBUMrwNmEHYwI6z6WJuRRa7THgjiFZptn5bFL_blQMFFUBFZwRSyiJXzEzKnyV2JF-rp4-CP1bcPHXC76elERuMpGnz_8YsVf-2g9rVAU7z5Z8ZwTkxqlRQ-yfLxyQ-AUpyt6qMFXtT1WgJbFMbqBZo3MCUaDY3gZIetfQL5WlxFfRqtY5q1vX5TZxBmNn6uYY6Wf6NV4xnTxgoH3pNKh9cSZL45mREtn3id6jnyE_yORXjf81QCHxCCQivUvdGMltRmaTmlvc9dWT6ijOTDotVfC67iv5vxBC2yBS3bxDIGg.00bPVvQKhPfURjE-ghBvNg";

        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, makeGatewayMessage(applicationId, solutionId, gatewayType, workpathActionType, uiContextToken));

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithEmptyGatewaydata_ThenSkipTheMessageSilently() {

        StandardDeviceManagementService.getInstance().setApplicationContext(mockContext);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String emptyGatewaydata = "{\n" +
                "    \"gatewayMessage\": {\n" +
                "        \"details\": {\n" +
                "        },\n" +
                "        \"traceId\": 1\n" +
                "    }\n" +
                "}";
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, emptyGatewaydata);

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    @Test
    public void GivenGatewayMessageHandler_WhenGatewayMessageReceivedWithEmptyActionType_ThenSkipTheMessageSilently() {

        StandardDeviceManagementService.getInstance().setApplicationContext(mockContext);
        StandardDeviceManagementService.getInstance().setPackageManagerHelper(mockPmHelper);
        GatewayMessageHandler handler = new GatewayMessageHandler();
        String solutionId = "11111111-1111-1111-9999-111111111111";
        String emptyActionType = "{\n" +
                "    \"gatewayMessage\": {\n" +
                "        \"details\": {\n" +
                "            \"workpathGatewayData\": {\n" +
                "            }\n" +
                "        },\n" +
                "        \"traceId\": 1\n" +
                "    }\n" +
                "}";
        Mockito.when(mockPmHelper.getSolutionId(Mockito.any(Context.class), Mockito.anyString())).thenReturn(solutionId);

        handler.onReceived(0, emptyActionType);

        //verify
        String token = StandardDeviceManagementService.getInstance().getUiContextToken("testPackageName");
        assertTrue(token.isEmpty());
    }

    private String makeGatewayMessage(String applicationId, String solutionId, String gatewayType, String workpathActionType, String uiContextToken) {
        return "{\n" +
                "    \"gatewayMessage\": {\n" +
                "        \"details\": {\n" +
                "            \"workpathGatewayData\": {\n" +
                "                \"applicationId\": \"" + applicationId + "\",\n" +
                "                \"gatewayType\": \"" + gatewayType + "\",\n" +
                "                \"solutionId\": \"" + solutionId + "\",\n" +
                "                \"uiContextToken\": \"" + uiContextToken + "\",\n" +
                "                \"workpathActionType\": \"" + workpathActionType + "\"\n" +
                "            }\n" +
                "        },\n" +
                "        \"traceId\": 1\n" +
                "    }\n" +
                "}";
    }
}

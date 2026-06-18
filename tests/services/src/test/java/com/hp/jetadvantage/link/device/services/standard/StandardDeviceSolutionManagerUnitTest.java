package com.hp.jetadvantage.link.device.services.standard;

import static com.hp.ext.types.solutionManager.NotificationType.NtConfigurationModified;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelPayloadValue;
import static com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageTestHelper.makeTestChannelSetupMessage;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.ext.clients.OXPdHttpRequestException;
import com.hp.ext.clients.ResourceFacadeHelper;
import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.ext.types.base.Link;
import com.hp.ext.types.common.ServiceMetadataImpl;
import com.hp.ext.types.common.ServicesDiscoveryImpl;
import com.hp.ext.types.solutionManager.SolutionNotification;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.exceptions.IllegalSolutionException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageHandler;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;

@RunWith(MockitoJUnitRunner.class)
public class StandardDeviceSolutionManagerUnitTest extends StandardDeviceUnitTest {
    private static final String testSolutionId = "1111111-1111-1111-9995-111111111111";
    private static final String testSolutionToken = "testSolutionToken";

    private static String getBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        try (InputStream inputStream = bodyPart.getInputStream()) {
            byte[] targetArray = new byte[inputStream.available()];
            inputStream.read(targetArray);
            return new String(targetArray);
        }
    }

    private void setupMockDeviceManagementService() {
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(true);
        when(mockDeviceManagementService.getDeviceIPAddress()).thenReturn("localhost");
        when(mockDeviceManagementService.getDiscoveryTree()).thenReturn(null);
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(testSolutionId);
        when(mockDeviceManagementService.getSolutionToken(eq(testPackageName))).thenReturn(testSolutionToken);

    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationCalledAndSolutionIdNotFound_ThenThrowIllegalSolutionException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfiguration
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalSolutionException.class, () -> {
            solutionManager.getConfiguration(testPackageName);
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationCalledAndSolutionTokenNotFound_ThenThrowIllegalSolutionException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(testSolutionId);
        when(mockDeviceManagementService.getSolutionToken(eq(testPackageName))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfiguration
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalSolutionException.class, () -> {
            solutionManager.getConfiguration(testPackageName);
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationAndDeviceNotConnected_ThenThrowBoundDeviceException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(testSolutionId);
        when(mockDeviceManagementService.getSolutionToken(eq(testPackageName))).thenReturn(testSolutionToken);
        when(mockDeviceManagementService.isDeviceConnected()).thenReturn(false);

        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfiguration
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(BoundDeviceException.class, () -> {
            solutionManager.getConfiguration(testPackageName);
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfiguration_ThenReturnConfiguration() throws IOException {
        String contentType = "multipart/mixed; boundary=\"MIME_boundary_1a2afdc3\"";
        setupMockDeviceManagementService();

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "solutionManager" + "/GET_ext_solutionManager_v1_solutions_{solutionId}_configuration.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfiguration
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        Configuration config = solutionManager.getConfiguration(testPackageName);

        //Verify the expected response
        assertNotNull("Configuration Not Null", config);
        assertEquals("MimeType", "application/json", config.getMimeType());
        assertNotNull("links", config.getLinks());
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenModifyConfiguration_ThenReturnConfiguration() throws IOException {
        String contentType = "multipart/mixed; boundary=\"MIME_boundary_1a2afdc3\"";
        setupMockDeviceManagementService();

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "solutionManager" + "/GET_ext_solutionManager_v1_solutions_{solutionId}_configuration.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfiguration
        Configuration_Modify configModify = new Configuration_Modify();
        configModify.setDescription("New Description");
        configModify.setMimeType("application/json");
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        Configuration config = solutionManager.modifyConfiguration(testPackageName, configModify);

        //Verify the expected response
        assertNotNull("Configuration Not Null", config);
        assertEquals("MimeType", "application/json", config.getMimeType());
        assertNotNull("links", config.getLinks());
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationData_ThenReturnDataString() throws IOException {
        String contentType = "multipart/mixed; boundary=\"MIME_boundary_1a2afdc3\"";
        setupMockDeviceManagementService();

        String response = Utils.loadTestTxtResource(Objects.requireNonNull(getClass().getClassLoader()),
                "solutionManager" + "/GET_ext_solutionManager_v1_solutions_{solutionId}_configuration_data.txt");
        ResponseBody responseBody = ResponseBody.create(response, MediaType.get(contentType));
        when(mockHttpClient.getResponseBody(any(Request.class))).thenReturn(responseBody);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        String data = solutionManager.getConfigurationData(testPackageName);

        //Verify the expected response
        JsonObject jsonObject = new JsonParser().parse(data).getAsJsonObject();
        assertTrue(jsonObject.has("thisNewConfigKey1"));
        assertTrue(jsonObject.has("thatNewConfigKey2"));
        assertEquals("This New value1", jsonObject.get("thisNewConfigKey1").getAsString());
        assertEquals("That New value2", jsonObject.get("thatNewConfigKey2").getAsString());
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationDataCalledAndHttpClientThrowIOException_ThenThrowOXPdHttpRequestException() throws IOException {
        setupMockDeviceManagementService();

        when(mockHttpClient.getResponseBody(any(Request.class))).thenThrow(new IOException("Mock IOException"));
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        OXPdHttpRequestException exception = assertThrows(OXPdHttpRequestException.class, () -> {
            solutionManager.getConfigurationData(testPackageName);
        });
        assertTrue(exception.getMessage().contains("Mock IOException"));
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationDataCalledAndSolutionIdNotFound_ThenThrowIllegalSolutionException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalSolutionException.class, () -> {
            solutionManager.getConfigurationData(testPackageName);
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenGetConfigurationDataCalledAndSolutionTokenNotFound_ThenThrowIllegalSolutionException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(testSolutionId);
        when(mockDeviceManagementService.getSolutionToken(eq(testPackageName))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : getConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalSolutionException.class, () -> {
            solutionManager.getConfigurationData(testPackageName);
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenReplaceConfigurationDataCalled_ThenReturnDataString() throws IOException, MessagingException {
        String replaceData = "{\"replacedKey1\":\"Replaced New value1\",\"replacedKey2\":\"Replaced New value2\"}";
        setupMockDeviceManagementService();

        String response = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "solutionManager" + "/PUT_ext_solutionManager_v1_solutions_{solutionId}_configuration_data.json");
        when(mockHttpClient.getResponseAsString(any(Request.class))).thenReturn(response);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : replaceConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        InputStream newConfigData = new ByteArrayInputStream(replaceData.getBytes());
        solutionManager.replaceConfigurationData(testPackageName, newConfigData);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(mockHttpClient).getResponseAsString(requestCaptor.capture());
        Request capturedRequest = requestCaptor.getValue();

        assertTrue(capturedRequest.body().contentType().toString().matches("multipart/mixed; boundary=.*"));
        assertTrue(capturedRequest.header("Content-Type").matches("multipart/mixed; boundary=.*"));
        assertTrue(capturedRequest.body() instanceof MultipartBody);
        MultipartBody multipartBody = (MultipartBody) capturedRequest.body();
        assertEquals(2, multipartBody.size());

        assertEquals("attachment; name=\"content\"", multipartBody.part(0).headers().get("Content-Disposition"));
        assertEquals("attachment; name=\"data\"", multipartBody.part(1).headers().get("Content-Disposition"));

        String requestStr = requestBodyToString(capturedRequest);
        ByteArrayDataSource datasource = new ByteArrayDataSource(requestStr.getBytes(),
                capturedRequest.body().contentType().toString());
        MimeMultipart multipart = new MimeMultipart(datasource);

        JsonObject jsonObject = new JsonParser().parse(getBodyPart(multipart.getBodyPart(1))).getAsJsonObject();
        assertTrue(jsonObject.has("replacedKey1"));
        assertTrue(jsonObject.has("replacedKey2"));
        assertEquals("Replaced New value1", jsonObject.get("replacedKey1").getAsString());
        assertEquals("Replaced New value2", jsonObject.get("replacedKey2").getAsString());
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenReplaceConfigurationDataCalledAndHttpClientThrowIOException_ThenThrowOXPdHttpRequestException() throws IOException {
        String replaceData = "{\"replacedKey1\":\"Replaced New value1\",\"replacedKey2\":\"Replaced New value2\"}";
        setupMockDeviceManagementService();

        when(mockHttpClient.getResponseAsString(any(Request.class))).thenThrow(new IOException("Mock IOException"));
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : replaceConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        InputStream newConfigData = new ByteArrayInputStream(replaceData.getBytes());
        OXPdHttpRequestException exception = assertThrows(OXPdHttpRequestException.class, () -> {
            solutionManager.replaceConfigurationData(testPackageName, newConfigData);
        });
        assertTrue(exception.getMessage().contains("Mock IOException"));
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenReplaceConfigurationDataCalledAndSolutionIdNotFound_ThenThrowIllegalSolutionException() {
        when(mockDeviceManagementService.getSolutionId(eq(testPackageName))).thenReturn(null);
        ResourceFacadeHelper.setHttpClient(mockHttpClient);

        //invoke target test method : replaceConfigurationData
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalSolutionException.class, () -> {
            solutionManager.replaceConfigurationData(testPackageName, new ByteArrayInputStream("".getBytes()));
        });
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenRegisterNotificationCallbackCalledAndNotificationReceived_ThenCallbackShouldBeCalled() {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String typeGUN = new SolutionNotification().getTypeGUN();
        String packageId = "app1";
        String sampleSolutionNotification = "{\"notificationType\":\"ntConfigurationModified\"}";

        setupMockDeviceManagementService();

        //define test callback
        AtomicInteger callbackCount = new AtomicInteger(0);
        IE2PayloadCallback<SolutionNotification> testCallback = new IE2PayloadCallback<SolutionNotification>() {
            @Override
            public void onReceiveNotification(String appPackageId, SolutionNotification notification) {
                callbackCount.incrementAndGet();
                assertEquals("app1", appPackageId);
                assertEquals("NtConfigurationModified", NtConfigurationModified, notification.getNotificationType());
            }
        };

        //invoke target test method : registerNotificationCallback
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        solutionManager.registerNotificationCallback(testCallback);

        //inject test solution notification message
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId,
                StandardDeviceSolutionManager.E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN));
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleSolutionNotification));

        //verify the registered callback is called
        assertEquals("callback call#", 1, callbackCount.get());
        callbackCount.set(0);

        //unregister callback
        solutionManager.unRegisterNotificationCallback();

        //inject test solution notification message again
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleSolutionNotification));

        //verify the callback is not called
        assertEquals("callback call#", 0, callbackCount.get());
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenRegisterNotificationCallbackCalledAndCallbackThrowException_ThenExceptionShouldBeHandled() {
        String channelId = "223762e2-8bf7-4e29-a1d7-0790103e83a7";
        String typeGUN = new SolutionNotification().getTypeGUN();
        String packageId = "app1";
        String sampleSolutionNotification = "{\"notificationType\":\"ntConfigurationModified\"}";

        setupMockDeviceManagementService();

        //define test callback
        AtomicInteger callbackCount = new AtomicInteger(0);
        IE2PayloadCallback<SolutionNotification> testCallback = new IE2PayloadCallback<SolutionNotification>() {
            @Override
            public void onReceiveNotification(String appPackageId, SolutionNotification notification) {
                callbackCount.incrementAndGet();
                assertEquals("app1", appPackageId);
                assertEquals("NtConfigurationModified", NtConfigurationModified, notification.getNotificationType());
                throw new RuntimeException("test exception");
            }
        };

        //invoke target test method : registerNotificationCallback
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        solutionManager.registerNotificationCallback(testCallback);

        //inject test solution notification message
        AppChannelMessageHandler handler = new AppChannelMessageHandler(mockWsCallbackService);
        handler.onReceived(0, makeTestChannelSetupMessage(channelId, packageId,
                StandardDeviceSolutionManager.E2SERVICE_SOLUTION_MANAGER_CANONICAL_GUN));
        handler.onReceived(0, makeTestChannelPayloadValue(channelId, typeGUN, sampleSolutionNotification));

        //verify the registered callback is called
        assertEquals("callback call#", 1, callbackCount.get());
        callbackCount.set(0);
    }

    @Test
    public void GivenStandardDeviceSolutionManager_WhenRegisterNotificationCallbackCalledWithNullCallback_ThenThrowIllegalArgumentException() {
        setupMockDeviceManagementService();

        //invoke target test method : registerNotificationCallback
        IDeviceSolutionManager solutionManager = new StandardDeviceSolutionManager(mockDeviceManagementService);
        assertThrows(IllegalArgumentException.class, () -> {
            solutionManager.registerNotificationCallback(null);
        });
    }

    public ServicesDiscoveryImpl createBasicTestServicesDiscovery() {
        ServicesDiscoveryImpl resource = new ServicesDiscoveryImpl();
        resource.setVersion("1.0");
        resource.setServices(getServiceMetaData());
        return resource;
    }

    private List<ServiceMetadataImpl> getServiceMetaData() {
        List<ServiceMetadataImpl> serviceMetaData = new ArrayList<ServiceMetadataImpl>();
        ServiceMetadataImpl appServiceMetadata = new ServiceMetadataImpl();
        appServiceMetadata.setDescription("CDM SolutionManager Service");
        appServiceMetadata.setServiceGun("com.hp.cdm.service.solutionManager.version.1");
        appServiceMetadata.setLinks(geLinks());
        serviceMetaData.add(appServiceMetadata);
        return serviceMetaData;
    }

    private List<Link> geLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link();
        link.setHref("/cdm/solutionManager/v1/capabilities");
        link.setRel("capabilities");
        links.add(link);

        Link configurationLink = new Link();
        configurationLink.setHref("/cdm/solutionManager/v1/configuration");
        configurationLink.setRel("configuration");
        links.add(configurationLink);

        Link solutionsLink = new Link();
        solutionsLink.setHref("/cdm/solutionManager/v1/solutions");
        solutionsLink.setRel("solutions");
        links.add(solutionsLink);

        return links;
    }

    private String requestBodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null) {
                copy.body().writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "Did not work";
        }
    }
}

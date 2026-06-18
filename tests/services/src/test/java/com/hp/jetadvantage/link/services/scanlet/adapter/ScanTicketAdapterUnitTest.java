/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.net.Uri;
import android.os.Environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.EmailOptions;
import com.hp.ext.service.scanJob.FtpProtocolOptions;
import com.hp.ext.service.scanJob.HttpDestination;
import com.hp.ext.service.scanJob.NetworkFolderOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJobIdentifier;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.service.scanJob.TransmissionMode;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.CompressionType;
import com.hp.ext.types.imaging.ContentOrientation;
import com.hp.ext.types.imaging.FileFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.imaging.QualityVsSize;
import com.hp.ext.types.imaging.Resolution;
import com.hp.ext.types.imaging.ScanProgressMode;
import com.hp.ext.types.media.MediaInputId;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.ext.types.network.FtpTransferMode;
import com.hp.ext.types.network.NetworkFolderProtocol;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.ext.types.security.CredentialSourceType;
import com.hp.jetadvantage.link.api.scanner.EmailAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.NetworkCredentialsAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.SmtpAttributes;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.utils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class ScanTicketAdapterUnitTest {
    public String testPackageName = Constants.TEST_PACKAGE_NAME;

    @Mock
    private IDeviceScanJobService mockScanJobService;

    @Mock
    private ScanJob mockScanJob;

    private ObjectMapper mapper;
    private MockedStatic<Environment> mockedEnvironment;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        // Mock ScanJobService responses
        String profileResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_profile.json");
        when(mockScanJobService.getProfile(testPackageName))
                .thenReturn(mapper.readValue(profileResponse, Profile.class));
        String defaultOptionsResponse = Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()),
                "scanJob/GET_ext_scanJob_defaultOptions.json");
        defaultOptionsResponse = E2JsonTestHelper.simplifyE2Json(defaultOptionsResponse);
        when(mockScanJobService.getDefaultOptions(testPackageName))
                .thenReturn(mapper.readValue(defaultOptionsResponse, DefaultOptions.class));

        // Mock Environment static methods
        mockedEnvironment = mockStatic(Environment.class);
        File mockFile = new File("test");
        mockedEnvironment.when(() -> Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .thenReturn(mockFile);
        mockedEnvironment.when(Environment::getExternalStorageDirectory)
                .thenReturn(mockFile);
        mockedEnvironment.when(Environment::getDataDirectory)
                .thenReturn(mockFile);
    }

    @After
    public void tearDown() {
        // Close the mocked static Environment
        mockedEnvironment.close();
    }

    // Helper methods
    private ScanAttributesCaps getScanAttributesCaps() throws Exception {
        String capabilitiesJson = ScanOptionProfileAdapter.getCapabilities(testPackageName, mockScanJobService, ScanAttributes.TransmissionMode.JOB, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
        return JsonParser.getInstance().fromJson(capabilitiesJson, ScanAttributesCaps.class);
    }

    private FileOptionsAttributesCaps getFileOptionsCaps(ScanAttributes.ColorMode colorMode, ScanAttributes.DocumentFormat documentFormat) throws Exception {
        String fileOptionsJson = ScanFileOptionAdapter.getFileOptions(testPackageName, mockScanJobService, colorMode, documentFormat);
        return JsonParser.getInstance().fromJson(fileOptionsJson, FileOptionsAttributesCaps.class);
    }

    private ScanAttributes getDefaultScanAttributes() throws Exception {
        return ScanDefaultOptionAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService, ScanAttributes.Destination.HTTP, Sdk.VERSION.LEVEL);
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * ME MONO PDF_A
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithMeDestination_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate test ScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();
        FileOptionsAttributesCaps fileOptionsAttributesCaps = getFileOptionsCaps(
                ScanAttributes.ColorMode.MONO,
                ScanAttributes.DocumentFormat.PDF_A);

        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder()
                .setColorMode(ScanAttributes.ColorMode.MONO)
                .setDocumentFormat(ScanAttributes.DocumentFormat.PDF_A)
                .setDuplex(ScanAttributes.Duplex.NONE)
                .setResolution(ScanAttributes.Resolution.DPI_600)
                .setScanSize(ScanAttributes.ScanSize.A3)
                .setOrientation(ScanAttributes.Orientation.LANDSCAPE)
                //.setScanPreview(ScanAttributes.ScanPreview.TRUE)
                .setBackgroundCleanup(ScanAttributes.BackgroundCleanup.LEVEL_7)
                .setContrastAdjustment(ScanAttributes.ContrastAdjustment.LEVEL_7)
                //.setBlankImageRemovalMode(ScanAttributes.BlankImageRemovalMode.ON)
                .setProgressDialogMode(ScanAttributes.ProgressDialogMode.ON)
                .setOutputQuality(ScanAttributes.OutputQuality.HIGH)
                .setTransmissionMode(ScanAttributes.TransmissionMode.JOB)
                //.setSharpnessAdjustment(ScanAttributes.SharpnessAdjustment.LEVEL_3)
                //.setTextPhotoOptimization(ScanAttributes.TextPhotoOptimization.GRAPHIC)
                .setMediaSource(ScanAttributes.MediaSource.FLATBED)
                .setMisfeedDetectionMode(ScanAttributes.MisfeedDetectionMode.ON)
                .setAutomaticStraightenMode(ScanAttributes.AutomaticStraightenMode.ENABLE)
                .setFileName("test");

        FileOptionsAttributes.Builder fileOptionsAttributesBuilder = new FileOptionsAttributes.Builder()
                .setPdfEncryptionPassword("test");
        scanAttributeMeBuilder.setFileOptionsAttributes(
                fileOptionsAttributesBuilder.build(fileOptionsAttributesCaps));

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeMeBuilder.build(scanAttributeCaps));

        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isLocalFolder", scanTicket.getDestinationOptions().isLocalFolder());
        assertNotNull("MetadataOptions", scanTicket.getMetadataOptions());
        assertEquals("MediaType", "application/json", scanTicket.getMetadataOptions().getFileContentType().getExplicit().getExplicitValue().getValue());
        assertEquals("FileName", "metadata-$JOB(ID)$.json", scanTicket.getMetadataOptions().getFileName().getExplicit().getExplicitValue().getValue());
        assertEquals("Color Mode", ColorMode.CmMonochrome, scanTicket.getScanOptions().getColorMode());
        assertEquals("OutputFileFormat", FileFormat.FfPdfa, scanTicket.getScanOptions().getOutputFileFormat());
        assertEquals("PlexMode", PlexMode.PmSimplex, scanTicket.getScanOptions().getPlexMode());
        assertEquals("Resolution", Resolution.Dpi600, scanTicket.getScanOptions().getResolution());
        assertEquals("MediaSize", MediaSizeId.MsISO_A3_297x420mm, scanTicket.getScanOptions().getMediaSize());
        assertEquals("ContentOrientation", ContentOrientation.CoLandscape, scanTicket.getScanOptions().getContentOrientation());
        assertEquals("BackgroundCleanup", new Unsigned32(88L).getValue(), scanTicket.getScanOptions().getBackgroundCleanup().getValue());
        assertEquals("Contrast", new Unsigned32(7L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
        assertEquals("ScanProgressMode", ScanProgressMode.SpmStandard, scanTicket.getScanOptions().getScanProgressMode());
        assertEquals("OutputQualityVsSize", QualityVsSize.QvsHigh, scanTicket.getScanOptions().getOutputQualityVsSize());
        assertEquals("FileTransmissionMode", TransmissionMode.TmJob, scanTicket.getScanOptions().getFileTransmissionMode());
        assertEquals("MediaSource", MediaInputId.MiFlatbed, scanTicket.getScanOptions().getMediaSource());
        assertTrue("MisfeedDetection", scanTicket.getScanOptions().getMisfeedDetection());
        assertTrue("AutoDeskew", scanTicket.getScanOptions().getAutoDeskew());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
        assertTrue("OutputFileEncryption", scanTicket.getScanOptions().getOutputFileEncryption());
        assertEquals("EncryptionPassword", "test", scanTicket.getScanOptions().getEncryptionPassword());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : HTTP
     * Scan Options: COLOR TIFF BOOK ...
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithHttpDestination_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();
        FileOptionsAttributesCaps fileOptionsAttributesCaps = getFileOptionsCaps(
                ScanAttributes.ColorMode.COLOR,
                ScanAttributes.DocumentFormat.TIFF);

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("https");
        when(mockUri.getHost()).thenReturn("test.com");
        when(mockUri.getPort()).thenReturn(-1);
        when(mockUri.getPath()).thenReturn("/hp/scan");

        ScanAttributes.HttpBuilder scanAttributeHttpBuilder = new ScanAttributes.HttpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .build())
                .setColorMode(ScanAttributes.ColorMode.COLOR)
                .setDocumentFormat(ScanAttributes.DocumentFormat.TIFF)
                .setDuplex(ScanAttributes.Duplex.BOOK)
                .setBackgroundCleanup(ScanAttributes.BackgroundCleanup.LEVEL_1)
                .setContrastAdjustment(ScanAttributes.ContrastAdjustment.LEVEL_1)
                .setTransmissionMode(ScanAttributes.TransmissionMode.IMAGE)
                .setMediaSource(ScanAttributes.MediaSource.ADF)
                .setFileName("test");

        FileOptionsAttributes.Builder fileOptionsAttributesBuilder = new FileOptionsAttributes.Builder()
                .setTiffCompressionMode(FileOptionsAttributes.TiffCompressionMode.JPEG_TIFF_6);
        scanAttributeHttpBuilder.setFileOptionsAttributes(
                fileOptionsAttributesBuilder.build(fileOptionsAttributesCaps));

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeHttpBuilder.build(scanAttributeCaps));

        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isHttp", scanTicket.getDestinationOptions().isHttp());
        HttpDestination httpDestination = scanTicket.getDestinationOptions().getHttp().getDestination();
        assertEquals("Username", "admin", httpDestination.getAuthorization().getBasic().getUsername().getExplicit().getExplicitValue());
        assertEquals("Password", "password", httpDestination.getAuthorization().getBasic().getPassword().getExplicit().getExplicitValue());
        assertEquals("Host", "test.com", httpDestination.getHost().getExplicit().getExplicitValue().getValue());
        assertEquals("Path", "/hp/scan", httpDestination.getPath().getExplicit().getExplicitValue().getValue());
        assertEquals("Scheme", "https", scanTicket.getDestinationOptions().getHttp().getDestination().getScheme());
        assertEquals("MaxRetries", 0, httpDestination.getRetry().getBasic().getMaxRetries().getExplicit().getExplicitValue().intValue());
        assertEquals("TimeoutSeconds", 60, httpDestination.getRetry().getBasic().getTimeoutSeconds().getExplicit().getExplicitValue().intValue());
        assertNull("MetadataOptions", scanTicket.getMetadataOptions());
        assertEquals("ColorMode", ColorMode.CmColor, scanTicket.getScanOptions().getColorMode());
        assertEquals("OutputFileFormat", FileFormat.FfTiff, scanTicket.getScanOptions().getOutputFileFormat());
        assertEquals("PlexMode", PlexMode.PmDuplex, scanTicket.getScanOptions().getPlexMode());
        assertEquals("MediaBindingFormat", BindingFormat.BfFlipLeft, scanTicket.getScanOptions().getMediaBindingFormat());
        assertEquals("BackgroundCleanup", new Unsigned32(13L).getValue(), scanTicket.getScanOptions().getBackgroundCleanup().getValue());
        assertEquals("Contrast", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
        assertEquals("TransmissionMode", TransmissionMode.TmImage, scanTicket.getScanOptions().getFileTransmissionMode());
        assertEquals("MediaSource", MediaInputId.MiAdf, scanTicket.getScanOptions().getMediaSource());
        assertEquals("ScanOptions", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
        assertEquals("CompressionType", CompressionType.CtOJpeg, scanTicket.getScanOptions().getColorAndGrayCompression());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : HTTP
     * HTTP Options : Port number, empty path, empty network credentials, max retries, timeout
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithHttpDestinationAndEmptyPathCredentials_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("http");
        when(mockUri.getHost()).thenReturn("10.1.2.3");
        when(mockUri.getPort()).thenReturn(8080);
        when(mockUri.getPath()).thenReturn(null);

        ScanAttributes.HttpBuilder scanAttributeHttpBuilder = new ScanAttributes.HttpBuilder(mockUri)
                .setMaxConsecutiveRetries(5)
                .setConnectTimeout(10)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeHttpBuilder.build(scanAttributeCaps));

        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isHttp", scanTicket.getDestinationOptions().isHttp());
        HttpDestination httpDestination = scanTicket.getDestinationOptions().getHttp().getDestination();
        assertNull("Auth", httpDestination.getAuthorization());
        assertEquals("Host", "10.1.2.3:8080", httpDestination.getHost().getExplicit().getExplicitValue().getValue());
        assertEquals("Path", "/", httpDestination.getPath().getExplicit().getExplicitValue().getValue());
        assertEquals("Scheme", "http", scanTicket.getDestinationOptions().getHttp().getDestination().getScheme());
        assertEquals("MaxRetries", 5, httpDestination.getRetry().getBasic().getMaxRetries().getExplicit().getExplicitValue().intValue());
        assertEquals("TimeoutSeconds", 10, httpDestination.getRetry().getBasic().getTimeoutSeconds().getExplicit().getExplicitValue().intValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : HTTP
     * HTTP Options : Port number, valid path, valid network credentials, max retries, timeout
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithHttpDestinationAndPortPathCredentials_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("https");
        when(mockUri.getHost()).thenReturn("10.1.2.3");
        when(mockUri.getPort()).thenReturn(8080);
        when(mockUri.getPath()).thenReturn("/upload");

        ScanAttributes.HttpBuilder scanAttributeHttpBuilder = new ScanAttributes.HttpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .build())
                .setMaxConsecutiveRetries(5)
                .setConnectTimeout(10)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeHttpBuilder.build(scanAttributeCaps));

        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isHttp", scanTicket.getDestinationOptions().isHttp());
        HttpDestination httpDestination = scanTicket.getDestinationOptions().getHttp().getDestination();
        assertEquals("Host", "10.1.2.3:8080", httpDestination.getHost().getExplicit().getExplicitValue().getValue());
        assertEquals("Path", "/upload", httpDestination.getPath().getExplicit().getExplicitValue().getValue());
        assertEquals("Scheme", "https", scanTicket.getDestinationOptions().getHttp().getDestination().getScheme());
        assertEquals("MaxRetries", 5, httpDestination.getRetry().getBasic().getMaxRetries().getExplicit().getExplicitValue().intValue());
        assertEquals("TimeoutSeconds", 10, httpDestination.getRetry().getBasic().getTimeoutSeconds().getExplicit().getExplicitValue().intValue());
        assertEquals("Username", "admin", httpDestination.getAuthorization().getBasic().getUsername().getExplicit().getExplicitValue());
        assertEquals("Password", "password", httpDestination.getAuthorization().getBasic().getPassword().getExplicit().getExplicitValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : EMAIL
     * Scan Options : MONO TIFF FLIP ...
     */
    /*
    //This is not used now.
    //scanTicket.getDestinationOptions().isEmail() is not supported in the device side.
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithEmailDestination_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();
        FileOptionsAttributesCaps fileOptionsAttributesCaps = getFileOptionsCaps(
                ScanAttributes.ColorMode.MONO,
                ScanAttributes.DocumentFormat.TIFF);

        EmailAttributes.Builder emailAttributesBuilder = new EmailAttributes.Builder()
                .addToAddress("test1@test.com", "test1")
                .addToAddress("test2@test.com", "test2")
                .addCcAddress("cc1@test.com", "cc1")
                .addCcAddress("cc2@test.com", "cc2")
                .addBccAddress("bcc1@test.com", "bcc1")
                .addBccAddress("bcc2@test.com", "bcc2")
                .setFrom("from@test.com", "from")
                .setMessage("testMessage")
                .setSubject("testSubject");

        ScanAttributes.EmailBuilder scanAttributeEmailBuilder = new ScanAttributes.EmailBuilder(emailAttributesBuilder.build());

        SmtpAttributes.Builder smtpAttributesBuilder = new SmtpAttributes.Builder("test.com")
                .setPort(25)
                .setConnectTimeout(20)
                .setTransportMode(SmtpAttributes.TransportMode.SSL_TLS)
                .setServerCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .setDomain("test2.com")
                        .build())
                .setReadTimeout(10);
        scanAttributeEmailBuilder.setSmtpAttributes(smtpAttributesBuilder.build());

        scanAttributeEmailBuilder.setColorMode(ScanAttributes.ColorMode.MONO)
                .setDocumentFormat(ScanAttributes.DocumentFormat.TIFF)
                .setDuplex(ScanAttributes.Duplex.FLIP)
                .setFileName("test");

        FileOptionsAttributes.Builder fileOptionsAttributesBuilder = new FileOptionsAttributes.Builder()
                .setTiffCompressionMode(FileOptionsAttributes.TiffCompressionMode.G_4);
        scanAttributeEmailBuilder.setFileOptionsAttributes(
                fileOptionsAttributesBuilder.build(fileOptionsAttributesCaps));

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeEmailBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue(scanTicket.getDestinationOptions().isEmail());
        EmailOptions emailOptions = scanTicket.getDestinationOptions().getEmail();
        assertEquals("EmailOptions To", 2, emailOptions.getToList().size());
        assertEquals("EmailOptions CC", 2, emailOptions.getCcList().size());
        assertEquals("EmailOptions BCC", 2, emailOptions.getBccList().size());
        assertEquals("EmailOptions From", "from@test.com", emailOptions.getFrom().getAddress().getExplicit().getExplicitValue().toString());
        assertEquals("EmailOptions From DisplayName", "from", emailOptions.getFrom().getDisplayName().getExplicit().getExplicitValue());
        assertEquals("EmailOptions Body", "testMessage", emailOptions.getBody().getExplicit().getExplicitValue());
        assertEquals("EmailOptions Subject", "testSubject", emailOptions.getSubject().getExplicit().getExplicitValue());
        assertEquals("EmailOptions ServerName", "test.com", emailOptions.getServer().getServerName().getExplicit().getExplicitValue().toString());
        assertEquals("EmailOptions Port", 25, emailOptions.getServer().getServerPort().getExplicit().getExplicitValue().getValue().intValue());
        assertEquals("EmailOptions UserName", "admin", emailOptions.getServer().getCredential().getUser().getUserName().getExplicit().getExplicitValue());
        assertEquals("EmailOptions Password", "password", emailOptions.getServer().getCredential().getUser().getPassword().getExplicit().getExplicitValue());
        assertEquals("EmailOptions Domain", "test2.com", emailOptions.getServer().getCredential().getUser().getDomain().getExplicit().getExplicitValue());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, scanTicket.getDestinationOptions().getEmail().getServer().getCredentialSource());
        assertEquals("EmailOptions ServerAuth", false, emailOptions.getServer().getServerAuthenticationRequired().getExplicit().getExplicitValue());
        assertEquals("EmailOptions ServerValidation", false, emailOptions.getServer().getServerValidationRequired().getExplicit().getExplicitValue());
        assertEquals("EmailOptions UseTls", true, emailOptions.getServer().getUseTls().getExplicit().getExplicitValue());
        assertNotNull("MetadataOptions", scanTicket.getMetadataOptions());
        assertEquals("MediaType", "application/json", scanTicket.getMetadataOptions().getFileContentType().getExplicit().getExplicitValue().getValue());
        assertEquals("FileName", "metadata-$JOB(ID)$.json", scanTicket.getMetadataOptions().getFileName().getExplicit().getExplicitValue().getValue());
        assertEquals("ColorMode", ColorMode.CmMonochrome, scanTicket.getScanOptions().getColorMode());
        assertEquals("FileFormat", FileFormat.FfTiff, scanTicket.getScanOptions().getOutputFileFormat());
        assertEquals("PlexMode", PlexMode.PmDuplex, scanTicket.getScanOptions().getPlexMode());
        assertEquals("BindingFormat", BindingFormat.BfFlipUp, scanTicket.getScanOptions().getMediaBindingFormat());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
        assertEquals("CompressionType", CompressionType.CtG4, scanTicket.getScanOptions().getMonoCompression());
    }
     */

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : NETWORK_FOLDER
     * NETWORK_FOLDER Options : UNC path and network credentials
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithSmbDestination_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\d\\test");

        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                                .setUserName("admin")
                                .setPassword("password")
                                .setDomain("auth")
                                .build())
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpSmb, networkFolderOptions.getProtocol());
        assertEquals("UncPath", "\\\\d\\test",
                networkFolderOptions.getProtocolOptions().getSmbOptions().getUncPath().getExplicit().getExplicitValue().getValue());
        assertEquals("UserName", "admin", networkFolderOptions.getCredential().getUserName().getExplicit().getExplicitValue());
        assertEquals("Password", "password", networkFolderOptions.getCredential().getPassword().getExplicit().getExplicitValue());
        assertEquals("Domain", "auth", networkFolderOptions.getCredential().getDomain().getExplicit().getExplicitValue());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : NETWORK_FOLDER
     * NETWORK_FOLDER Options : UNC path and empty network credentials
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithSmbDestinationAndEmptyUserCredentials_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\10.2.3.4");

        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpSmb, networkFolderOptions.getProtocol());
        assertEquals("UncPath", "\\\\10.2.3.4",
                networkFolderOptions.getProtocolOptions().getSmbOptions().getUncPath().getExplicit().getExplicitValue().getValue());
        assertNull("Credential", networkFolderOptions.getCredential());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : NETWORK_FOLDER
     * NETWORK_FOLDER Options : UNC path and user credentials without domain
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithSmbDestinationAndCredentialsWithoutDomain_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\10.2.3.4\\shared\\folder");

        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                                .setUserName("admin")
                                .setPassword("password")
                                .build())
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpSmb, networkFolderOptions.getProtocol());
        assertEquals("UncPath", "\\\\10.2.3.4\\shared\\folder",
                networkFolderOptions.getProtocolOptions().getSmbOptions().getUncPath().getExplicit().getExplicitValue().getValue());
        assertEquals("UserName", "admin", networkFolderOptions.getCredential().getUserName().getExplicit().getExplicitValue());
        assertEquals("Password", "password", networkFolderOptions.getCredential().getPassword().getExplicit().getExplicitValue());
        assertNull("Domain", networkFolderOptions.getCredential().getDomain());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : NETWORK_FOLDER
     * NETWORK_FOLDER Options : UNC path and empty domain
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithSmbDestinationAndEmptyDomain_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\10.2.3.4\\shared\\folder");

        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                                .setUserName("admin")
                                .setPassword("password")
                                .setDomain("")
                                .build())
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpSmb, networkFolderOptions.getProtocol());
        assertEquals("UncPath", "\\\\10.2.3.4\\shared\\folder",
                networkFolderOptions.getProtocolOptions().getSmbOptions().getUncPath().getExplicit().getExplicitValue().getValue());
        assertEquals("UserName", "admin", networkFolderOptions.getCredential().getUserName().getExplicit().getExplicitValue());
        assertEquals("Password", "password", networkFolderOptions.getCredential().getPassword().getExplicit().getExplicitValue());
        assertNull("Domain", networkFolderOptions.getCredential().getDomain());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : FTP
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithFtpDestination_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("ftp://test.com/hp/scan");
        when(mockUri.getHost()).thenReturn("test.com");
        when(mockUri.getPath()).thenReturn("/hp/scan");
        when(mockUri.getPort()).thenReturn(-1);

        ScanAttributes.FtpBuilder scanAttributesFtpBuilder = new ScanAttributes.FtpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .setDomain("auth")
                        .build())
                .setDocumentFormat(ScanAttributes.DocumentFormat.JPEG)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributesFtpBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        FtpProtocolOptions ftpOptions = networkFolderOptions.getProtocolOptions().getFtpOptions();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpFtp, networkFolderOptions.getProtocol());
        assertEquals("Path", "/hp/scan", ftpOptions.getPath().getExplicit().getExplicitValue().toString());
        assertEquals("Port", 21, ftpOptions.getPort().getExplicit().getExplicitValue().getValue().intValue());
        assertEquals("Server", "test.com", ftpOptions.getServer().getExplicit().getExplicitValue().getValue());
        assertEquals("FtpTransferMode", FtpTransferMode.FtmPassive, ftpOptions.getTransferMode().getExplicit().getExplicitValue());
        assertEquals("UserName", "admin", networkFolderOptions.getCredential().getUserName().getExplicit().getExplicitValue());
        assertEquals("Password", "password", networkFolderOptions.getCredential().getPassword().getExplicit().getExplicitValue());
        assertEquals("Domain", "auth", networkFolderOptions.getCredential().getDomain().getExplicit().getExplicitValue());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());

        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
        assertEquals("OutputFileFormat", FileFormat.FfJpeg, scanTicket.getScanOptions().getOutputFileFormat());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket : Happy case - valid scan ticket
     * Destination : FTP
     * FTP Options : Port number, empty path, empty domain
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithDestinationFtpAndEmptyPathDomain_ThenValidScanTicketShouldBeReturned() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("ftp://10.1.2.3:221");
        when(mockUri.getHost()).thenReturn("10.1.2.3");
        when(mockUri.getPath()).thenReturn(null);
        when(mockUri.getPort()).thenReturn(221);

        ScanAttributes.FtpBuilder scanAttributesFtpBuilder = new ScanAttributes.FtpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .build())
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributesFtpBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call the target device adapter method: getScanTicket
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result ScanAttributes
        assertTrue("isNetworkFolder", scanTicket.getDestinationOptions().isNetworkFolder());
        NetworkFolderOptions networkFolderOptions = scanTicket.getDestinationOptions().getNetworkFolder();
        FtpProtocolOptions ftpOptions = networkFolderOptions.getProtocolOptions().getFtpOptions();
        //assertEquals("NetworkFolderProtocol", NetworkFolderProtocol.NfpFtp, networkFolderOptions.getProtocol());
        assertEquals("Path", "/", ftpOptions.getPath().getExplicit().getExplicitValue().toString());
        assertEquals("Port", 221, ftpOptions.getPort().getExplicit().getExplicitValue().getValue().intValue());
        assertEquals("Server", "10.1.2.3", ftpOptions.getServer().getExplicit().getExplicitValue().getValue());
        assertEquals("FtpTransferMode", FtpTransferMode.FtmPassive, ftpOptions.getTransferMode().getExplicit().getExplicitValue());
        assertEquals("UserName", "admin", networkFolderOptions.getCredential().getUserName().getExplicit().getExplicitValue());
        assertEquals("Password", "password", networkFolderOptions.getCredential().getPassword().getExplicit().getExplicitValue());
        assertNull("Domain", networkFolderOptions.getCredential().getDomain());
        assertEquals("CredentialSourceType", CredentialSourceType.CstProvided, networkFolderOptions.getCredentialSource());
        assertEquals("FileName", "test", scanTicket.getScanOptions().getFileName().getExplicit().getExplicitValue());
    }

    @Test
    public void GivenScanTicketAdapter_WhenCreateScanJobCalled_ThenJobIdShouldBeReturned() throws Exception {
        String expectedJobId = "3f0ee4f2-8b57-4e4b-8d3e-d6728e5f5c7b";
        ScanJobIdentifier scanJobIdentifier = new ScanJobIdentifier(UUID.fromString(expectedJobId));
        when(mockScanJob.getScanJobId()).thenReturn(scanJobIdentifier);
        when(mockScanJobService.createScanJob(eq(testPackageName), any(ScanJob_Create.class))).thenReturn(mockScanJob);

        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder().setFileName("test");
        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributeMeBuilder.build(scanAttributeCaps));

        // Call the target device adapter method: getScanTicket
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        String jobId = ScanTicketAdapter.createScanJob(testPackageName, mockScanJobService, scanAttributesReader, defaultScanAttributes);
        assertEquals("Valid JobId", expectedJobId, jobId);
    }

    @Test
    public void GivenScanTicketAdapter_WhenCreateScanJobCalledWithError_ThenNullShouldBeReturned() throws Exception {
        when(mockScanJobService.createScanJob(eq(testPackageName), any(ScanJob_Create.class))).thenReturn(null);

        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder().setFileName("test");
        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributeMeBuilder.build(scanAttributeCaps));

        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        String jobId = ScanTicketAdapter.createScanJob(testPackageName, mockScanJobService, scanAttributesReader, defaultScanAttributes);
        assertNull("Job creation failed, JobId", jobId);
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket :
     * Destination : NETWORK_FOLDER
     * useDestinationFromScanAttrs = false
     * Should use HTTP defaults (Contrast = 1) instead of NetworkFolder defaults (Contrast = 4)
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithUseDestinationFromScanAttrsFalse_ThenHttpDefaultsShouldBeUsed() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\networkfolder\\mock\\uri");

        // Do not set Contrast, so it picks up default
        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = false
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result
        assertTrue("Destination should be NETWORK_FOLDER", scanTicket.getDestinationOptions().isNetworkFolder());
        // HTTP default contrast is 1 (from JSON)
        // NetworkFolder default contrast is 4 (from JSON)
        assertEquals("Contrast should be HTTP default (1)", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket :
     * Destination : NETWORK_FOLDER
     * useDestinationFromScanAttrs = true
     * Should use NetworkFolder defaults (Contrast = 4)
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithUseDestinationFromScanAttrsTrue_ThenDestinationDefaultsShouldBeUsed() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("\\\\networkfolder\\mock\\uri");

        // Do not set Contrast, so it picks up default
        ScanAttributes.NetworkFolderBuilder scanAttributesNetworkFolderBuilder =
                new ScanAttributes.NetworkFolderBuilder(mockUri)
                        .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributesNetworkFolderBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), true, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = true
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result
        assertTrue("Destination should be NETWORK_FOLDER", scanTicket.getDestinationOptions().isNetworkFolder());
        // NetworkFolder default contrast is 4 (from JSON)
        assertEquals("Contrast should be NetworkFolder default (4)", new Unsigned32(4L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket :
     * Destination : FTP
     * useDestinationFromScanAttrs = false
     * Should use HTTP defaults (Contrast = 1) instead of FTP (NetworkFolder) defaults (Contrast = 4)
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithFtpDestinationAndUseDestinationFromScanAttrsFalse_ThenHttpDefaultsShouldBeUsed() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("ftp://test.com/hp/scan");
        when(mockUri.getHost()).thenReturn("test.com");
        when(mockUri.getPath()).thenReturn("/hp/scan");
        when(mockUri.getPort()).thenReturn(-1);

        // Do not set Contrast, so it picks up default
        ScanAttributes.FtpBuilder scanAttributesFtpBuilder = new ScanAttributes.FtpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .setDomain("auth")
                        .build())
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributesFtpBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = false
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result
        assertTrue("Destination should be NETWORK_FOLDER (FTP maps to NetworkFolder)", scanTicket.getDestinationOptions().isNetworkFolder());
        // HTTP default contrast is 1 (from JSON)
        assertEquals("Contrast should be HTTP default (1)", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    /**
     * Test for ScanDeviceAdapter.getScanTicket :
     * Destination : FTP
     * useDestinationFromScanAttrs = true
     * Should use FTP (NetworkFolder) defaults (Contrast = 4)
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithFtpDestinationAndUseDestinationFromScanAttrsTrue_ThenDestinationDefaultsShouldBeUsed() throws Exception {
        // Generate testScanAttributes
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.toString()).thenReturn("ftp://test.com/hp/scan");
        when(mockUri.getHost()).thenReturn("test.com");
        when(mockUri.getPath()).thenReturn("/hp/scan");
        when(mockUri.getPort()).thenReturn(-1);

        // Do not set Contrast, so it picks up default
        ScanAttributes.FtpBuilder scanAttributesFtpBuilder = new ScanAttributes.FtpBuilder(mockUri)
                .setNetworkCredentials(new NetworkCredentialsAttributes.Builder()
                        .setUserName("admin")
                        .setPassword("password")
                        .setDomain("auth")
                        .build())
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(scanAttributesFtpBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), true, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = true
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result
        assertTrue("Destination should be NETWORK_FOLDER (FTP maps to NetworkFolder)", scanTicket.getDestinationOptions().isNetworkFolder());
        // FTP uses NetworkFolder defaults, so contrast is 4 (from JSON)
        assertEquals("Contrast should be FTP/NetworkFolder default (4)", new Unsigned32(4L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    // ========================================================================================
    // AC3: useDestinationDefaults + Destination Combination Tests
    // ========================================================================================

    /**
     * Test for ScanTicketAdapter.getScanTicket :
     * Destination : HTTP
     * useDestinationFromScanAttrs = true with HTTP destination
     * Should use HTTP defaults (Contrast = 1)
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithHttpDestinationAndUseDestinationFromScanAttrsTrue_ThenHttpDefaultsShouldBeUsed() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        Uri mockUri = mock(Uri.class);
        when(mockUri.getScheme()).thenReturn("https");
        when(mockUri.getHost()).thenReturn("test.com");
        when(mockUri.getPort()).thenReturn(-1);
        when(mockUri.getPath()).thenReturn("/hp/scan");

        // Do not set Contrast, so it picks up default
        ScanAttributes.HttpBuilder scanAttributeHttpBuilder = new ScanAttributes.HttpBuilder(mockUri)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeHttpBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), true, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = true and HTTP destination
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result - even with useDestinationFromScanAttrs=true, HTTP destination → HTTP defaults
        assertTrue("Destination should be HTTP", scanTicket.getDestinationOptions().isHttp());
        // HTTP default contrast is 1
        assertEquals("Contrast should be HTTP default (1)", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    /**
     * Test for ScanTicketAdapter.getScanTicket :
     * Destination : ME
     * useDestinationFromScanAttrs = true
     * Currently ME falls through to HTTP defaults (TODO: DUNE-169946), so Contrast = 1
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithMeDestinationAndUseDestinationFromScanAttrsTrue_ThenHttpDefaultsShouldBeUsedTemporarily() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // ME destination — do not set Contrast, so it picks up default
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder()
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeMeBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), true, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = true and ME destination
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result — ME currently uses HTTP defaults (TODO: DUNE-169946 will change to JobStorage)
        assertTrue("Destination should be ME (localFolder)", scanTicket.getDestinationOptions().isLocalFolder());
        // Currently HTTP default contrast=1 (when DUNE-169946 is implemented, should be JobStorage contrast=4)
        assertEquals("Contrast should be HTTP default (1)", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    /**
     * Test for ScanTicketAdapter.getScanTicket :
     * Destination : ME
     * useDestinationFromScanAttrs = false
     * Should use HTTP defaults (Contrast = 1) regardless of ME destination
     */
    @Test
    public void GivenScanTicketAdapter_WhenGetScanTicketCalledWithMeDestinationAndUseDestinationFromScanAttrsFalse_ThenHttpDefaultsShouldBeUsed() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // ME destination — do not set Contrast, so it picks up default
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder()
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeMeBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        // Call with useDestinationFromScanAttrs = false
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // Validate result — even ME destination falls back to HTTP defaults when useDestinationFromScanAttrs=false
        assertTrue("Destination should be ME (localFolder)", scanTicket.getDestinationOptions().isLocalFolder());
        // HTTP default contrast is 1 (not JobStorage's 4)
        assertEquals("Contrast should be HTTP default (1)", new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    // ========================================================================================
    // AC3: Merge Logic — User Specified Values + Default Values
    // ========================================================================================

    /**
     * Test for ScanTicketAdapter.getScanTicket :
     * Merge Logic: User sets some options (ColorMode, Resolution), defaults fill the rest.
     * Verifies user-specified values take precedence and defaults apply to unset options.
     */
    @Test
    public void GivenScanTicketAdapter_WhenUserSetsPartialOptions_ThenMergeWithDefaults() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // User explicitly sets ColorMode and Resolution, but NOT Contrast or OutputFileFormat
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder()
                .setColorMode(ScanAttributes.ColorMode.MONO)
                .setDocumentFormat(ScanAttributes.DocumentFormat.PDF)
                .setResolution(ScanAttributes.Resolution.DPI_600)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeMeBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // User-specified values should take precedence
        assertEquals("ColorMode should be user-specified MONO",
                ColorMode.CmMonochrome, scanTicket.getScanOptions().getColorMode());
        assertEquals("Resolution should be user-specified DPI_600",
                Resolution.Dpi600, scanTicket.getScanOptions().getResolution());

        // Non-specified values should use defaults (HTTP defaults when useDestinationFromScanAttrs=false)
        assertEquals("OutputFileFormat should use default (PDF)",
                FileFormat.FfPdf, scanTicket.getScanOptions().getOutputFileFormat());
        assertEquals("Contrast should use HTTP default (1)",
                new Unsigned32(1L).getValue(), scanTicket.getScanOptions().getContrast().getValue());
    }

    // ========================================================================================
    // AC3: DEFAULT Value Handling
    // ========================================================================================

    /**
     * Test for ScanTicketAdapter.getScanTicket :
     * When app sends DEFAULT for an option, device default value should be used instead.
     * DEFAULT is treated as "not specified" — falls through to device defaults.
     */
    @Test
    public void GivenScanTicketAdapter_WhenUserSetsDefaultValue_ThenDeviceDefaultShouldBeUsed() throws Exception {
        ScanAttributesCaps scanAttributeCaps = getScanAttributesCaps();

        // User explicitly sets ColorMode to DEFAULT
        ScanAttributes.MeBuilder scanAttributeMeBuilder = new ScanAttributes.MeBuilder()
                .setColorMode(ScanAttributes.ColorMode.DEFAULT)
                .setResolution(ScanAttributes.Resolution.DEFAULT)
                .setFileName("test");

        ScanAttributesReader scanAttributesReader = new ScanAttributesReader(
                scanAttributeMeBuilder.build(scanAttributeCaps));
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(testPackageName, mockScanJobService,
                scanAttributesReader.getDestination(), false, Sdk.VERSION.LEVEL);
        ScanTicket scanTicket = ScanTicketAdapter.getScanTicket(scanAttributesReader, defaultScanAttributes);

        // DEFAULT value should fall through to device default (HTTP: cmColor → COLOR)
        assertEquals("ColorMode should use device default (COLOR)",
                ColorMode.CmColor, scanTicket.getScanOptions().getColorMode());
        // DEFAULT resolution should fall through to device default (HTTP: dpi300 → DPI_300)
        assertEquals("Resolution should use device default (DPI_300)",
                Resolution.Dpi300, scanTicket.getScanOptions().getResolution());
    }
}

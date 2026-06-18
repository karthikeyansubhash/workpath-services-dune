package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelRegistryUnitTest {

    @Mock
    private StandardWebsocketCallbackService mockWebsocketService;

    private AppChannelRegistry registry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registry = new AppChannelRegistry(mockWebsocketService);
    }

    @Test
    public void GivenAppChannelRegistry_WhenAddChannelCalled_ThenChannelAdded() {
        AppChannelSetup setup = mock(AppChannelSetup.class);
        String channelId = "testChannel";

        boolean result = registry.addChannel(channelId, setup);

        assertTrue(result);
        assertEquals(setup, registry.getChannel(channelId));
    }

    @Test
    public void GivenAppChannelRegistry_WhenAddDuplicateChannel_ThenReturnFalse() {
        AppChannelSetup setup = mock(AppChannelSetup.class);
        String channelId = "testChannel";

        registry.addChannel(channelId, setup);
        boolean result = registry.addChannel(channelId, setup);

        assertFalse(result);
    }

    @Test
    public void GivenAppChannelRegistry_WhenRemoveChannelCalled_ThenChannelRemoved() {
        AppChannelSetup setup = mock(AppChannelSetup.class);
        String channelId = "testChannel";

        registry.addChannel(channelId, setup);
        registry.removeChannel(channelId);

        assertNull(registry.getChannel(channelId));
    }

    @Test
    public void GivenAppChannelRegistry_WhenGetPackageIdCalled_ThenChannelIdReturned() {
        AppChannelSetup setup = mock(AppChannelSetup.class);
        String channelId = "testChannel";
        String packageId = "testPackage";

        when(setup.getPackageId()).thenReturn(packageId);
        registry.addChannel(channelId, setup);

        assertEquals(packageId, registry.getPackageId(channelId));
    }

    @Test
    public void GivenAppChannelRegistry_WhenClearChannelsCalled_ThenChannelCleared() {
        AppChannelSetup setup = mock(AppChannelSetup.class);
        String channelId = "testChannel";

        registry.addChannel(channelId, setup);
        registry.clearChannels();

        assertNull(registry.getChannel(channelId));
    }
}

package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import com.hp.jetadvantage.link.device.services.standard.services.appchannel.AppChannelMessageQueue;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AppChannelMessageQueueUnitTest {

    private AppChannelMessageQueue messageQueue;

    @Mock
    private ChannelMessage mockMessage;

    private static final String TEST_KEY = "testKey";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        messageQueue = new AppChannelMessageQueue();
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenAddValidMessage_ThenReturnTrue() {
        // When adding a valid message
        boolean result = messageQueue.addMessage(TEST_KEY, mockMessage);

        // Then it should return true and message should be in queue
        assertTrue(result);
        assertFalse(messageQueue.isEmpty(TEST_KEY));
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenAddNullKey_ThenReturnFalse() {
        // When adding a message with null key
        boolean result = messageQueue.addMessage(null, mockMessage);

        // Then it should return false
        assertFalse(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenAddNullMessage_ThenReturnFalse() {
        // When adding a null message
        boolean result = messageQueue.addMessage(TEST_KEY, null);

        // Then it should return false
        assertFalse(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenPollMessage_ThenReturnAndRemoveMessage() {
        // Given a message in the queue
        messageQueue.addMessage(TEST_KEY, mockMessage);

        // When polling the message
        ChannelMessage result = messageQueue.pollMessage(TEST_KEY);

        // Then it should return the message and the queue should be empty
        assertEquals(mockMessage, result);
        assertTrue(messageQueue.isEmpty(TEST_KEY));
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenPollFromEmptyQueue_ThenReturnNull() {
        // When polling from a non-existent queue
        ChannelMessage result = messageQueue.pollMessage(TEST_KEY);

        // Then it should return null
        assertNull(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenPollFromNullKey_ThenReturnNull() {
        // When polling with null key
        ChannelMessage result = messageQueue.pollMessage(null);

        // Then it should return null
        assertNull(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenIsEmptyCalledOnEmptyQueue_ThenReturnTrue() {
        // When checking if a non-existent queue is empty
        boolean result = messageQueue.isEmpty(TEST_KEY);

        // Then it should return true
        assertTrue(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenIsEmptyCalledOnNonEmptyQueue_ThenReturnFalse() {
        // Given a message in the queue
        messageQueue.addMessage(TEST_KEY, mockMessage);

        // When checking if queue is empty
        boolean result = messageQueue.isEmpty(TEST_KEY);

        // Then it should return false
        assertFalse(result);
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenClearQueue_ThenAllQueuesAreEmptied() {
        // Given messages in multiple queues
        messageQueue.addMessage(TEST_KEY, mockMessage);
        messageQueue.addMessage("anotherKey", mockMessage);

        // When clearing all queues
        messageQueue.clearQueue();

        // Then all queues should be empty
        assertTrue(messageQueue.isEmpty(TEST_KEY));
        assertTrue(messageQueue.isEmpty("anotherKey"));
    }

    @Test
    public void GivenAppChannelMessageQueue_WhenQueueFull_ThenAddMessageReturnsFalse() throws Exception {
        // Create a queue with limited size
        LinkedBlockingQueue<ChannelMessage> limitedQueue = new LinkedBlockingQueue<>(1);
        ConcurrentHashMap<String, LinkedBlockingQueue<ChannelMessage>> queueMap = new ConcurrentHashMap<>();
        queueMap.put(TEST_KEY, limitedQueue);

        // Use reflection to replace the queue map
        Field mapField = AppChannelMessageQueue.class.getDeclaredField("channelMsgQueueMap");
        mapField.setAccessible(true);
        mapField.set(messageQueue, queueMap);

        // Add a message to fill the queue
        messageQueue.addMessage(TEST_KEY, mockMessage);

        // When adding another message to a full queue
        boolean result = messageQueue.addMessage(TEST_KEY, mockMessage);

        // Then it should return false
        assertFalse(result);
    }
}

package com.cisco.collab.kafkaclient.consumer;

import java.util.Map;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cisco.collab.kafkaclient.common.ConsumerCallback;
import com.cisco.collab.kafkaclient.exceptions.IncompleteInitializationException;
import com.cisco.collab.kafkaclient.exceptions.InvalidOperationException;

public class SimpleStringConsumerServiceTest {

    @Mock
    SimpleStringConsumer mockConsumer;    
    
    @Mock
    Map<String, SimpleStringConsumer> consumersByTopic;
    
    @BeforeMethod
    void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(consumersByTopic.get(Mockito.anyString())).thenReturn(mockConsumer);
    }

    public static final String TestTopic = "test";

    @Test
    public void testInitializationCheck() {
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        try {
            consumer.subscribe(TestTopic, cb, true);
            Assert.fail("did not check for initialization");
        } catch (IncompleteInitializationException e) {
            // noop
        }
        Mockito.verify(mockConsumer, Mockito.times(0)).addCallback(cb, true);
    }

    @Test
    public void testSubscription() throws Exception {
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        consumer.afterPropertiesSet();
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        consumer.subscribe(TestTopic, cb, true);
        Mockito.verify(mockConsumer).addCallback(cb, true);
    }

    @Test
    public void testUnSubscription() throws Exception {
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        consumer.afterPropertiesSet();
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        consumer.subscribe(TestTopic, cb, true);
        consumer.unsubscribe(TestTopic, cb);
        Mockito.verify(mockConsumer).addCallback(cb, true);
    }

    @Test
    public void testTopicCheckUnSubscription() throws Exception {
        Mockito.when(consumersByTopic.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        consumer.afterPropertiesSet();
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        try {
            consumer.unsubscribe(TestTopic, cb);
            Assert.fail("unsubscribe did not check for topic subscription");
        } catch (InvalidOperationException e) {
            // noop
        }
        Mockito.verify(mockConsumer, Mockito.times(0)).addCallback(cb, true);
    }

    @Test
    public void testCommit() throws Exception {
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        consumer.afterPropertiesSet();
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        consumer.subscribe(TestTopic, cb, true);
        consumer.commit(TestTopic);
        Mockito.verify(mockConsumer).commit(TestTopic);
    }

    @Test
    public void testTopicCheckCommit() throws Exception {
        Mockito.when(consumersByTopic.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockConsumer.addCallback(Mockito.any(ConsumerCallback.class),  Mockito.anyBoolean())).thenReturn(true);
        SimpleStringConsumerService consumer = new SimpleStringConsumerService(consumersByTopic);
        consumer.afterPropertiesSet();
        try {
            consumer.commit(TestTopic);
            Assert.fail("commit did not check for topic subscription");
        } catch (InvalidOperationException e) {
            // noop
        }
        Mockito.verify(mockConsumer, Mockito.times(0)).commit(TestTopic);
    }
}

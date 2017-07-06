package com.cisco.collab.kafkaclient.consumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleStringConsumerTest {
    public static final String TestTopic = "TestTopic";
    
    @Mock
    Consumer<String, String> kafka;
    
    @BeforeMethod
    void reset() {
        MockitoAnnotations.initMocks(this);
    }
    
    ConsumerRecords<String,String> mockRecords(String topic, String key, String value, int partition, int offset) {
        ConsumerRecord<String, String> record = new ConsumerRecord<String, String>(topic, partition, offset, key, value);
        Map<TopicPartition, List<ConsumerRecord<String, String>>> records = new HashMap<TopicPartition, List<ConsumerRecord<String, String>>>();
        records.put(new TopicPartition(topic, partition), Arrays.asList(record));
        return new ConsumerRecords<String, String>(records);
    }
    
    @Test
    public void testStartAndShutdown() throws InterruptedException {
        SimpleStringConsumer consumer = new SimpleStringConsumer(TestTopic, kafka);
        Thread.sleep(1000L);
        Assert.assertTrue(consumer.getThread().isAlive());
        consumer.shutdown();
        Thread.sleep(1000L);
        Assert.assertFalse(consumer.getThread().isAlive());        
    }

    @Test
    public void testSubscriptionRecieve() throws InterruptedException {
        Mockito.when(kafka.poll(Mockito.anyLong())).thenReturn(mockRecords("test_topic", "test_key", "test_message", 1, 0));
        SimpleStringConsumer consumer = new SimpleStringConsumer("test_topic", kafka);
        TestStringConsumerCallback cb = new TestStringConsumerCallback();
        consumer.addCallback(cb, true);
        Thread.sleep(1000L);
        Assert.assertEquals(cb.getRecords().count(), 1);
        ConsumerRecord<String, String> record = cb.getRecords().iterator().next();
        Assert.assertEquals(record.topic(), "test_topic");
        Assert.assertEquals(record.key(), "test_key");
        Assert.assertEquals(record.value(), "test_message");
        consumer.shutdown();
    }
}

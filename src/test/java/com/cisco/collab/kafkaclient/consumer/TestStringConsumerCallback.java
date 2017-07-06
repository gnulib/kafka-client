package com.cisco.collab.kafkaclient.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.cisco.collab.kafkaclient.common.ConsumerCallback;

public class TestStringConsumerCallback implements ConsumerCallback {

    ConsumerRecords<String, String> records = null;
    String topic = null;
    
    public TestStringConsumerCallback() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> void consume(String topic, ConsumerRecords<K, V> records) {
        this.topic = topic;
        this.records = (ConsumerRecords<String, String>) records;
    }
    
    String getTopic() {
        return topic;
    }
    
    ConsumerRecords<String, String> getRecords() {
        return records;
    }

}

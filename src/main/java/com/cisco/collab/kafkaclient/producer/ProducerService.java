package com.cisco.collab.kafkaclient.producer;

import org.apache.kafka.clients.producer.Callback;

public interface ProducerService {
    // blocking send request, returns true/false for success/failure
    <K, V> boolean send(String topic, K key, V value);
    
    // non-blocking send request, uses callback to report success/failure
    <K, V> void send(String topic, K key, V value, Callback cb);
}

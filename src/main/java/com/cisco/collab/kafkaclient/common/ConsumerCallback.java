package com.cisco.collab.kafkaclient.common;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * interface to be implemented by a callback that will receive notification with
 * records for the subscribed topic
 * 
 * @author bhadoria
 *
 */
public interface ConsumerCallback {
    <K, V> void consume(String topic, ConsumerRecords<K, V> records);
}

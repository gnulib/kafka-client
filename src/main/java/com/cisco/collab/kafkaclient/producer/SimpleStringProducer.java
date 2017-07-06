package com.cisco.collab.kafkaclient.producer;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.cisco.collab.kafkaclient.exceptions.InvalidConfigException;
import com.cisco.collab.kafkaclient.exceptions.KafkaClientException;

public class SimpleStringProducer implements ProducerService, InitializingBean {
    private static Logger LOG = Logger.getLogger(SimpleStringProducer.class.getName());
    
    static final String serializer = "org.apache.kafka.common.serialization.StringSerializer";

    @Value("${bootstrap.servers}")
    private String bootstrapServers;
    
    @Value("${client.id}")
    private String clientId;
    
    @Value("${acks:}")
    private String acks;
    
    @Value("${retries:}")
    private String retries;

    @Value("${batch.size:}")
    private String batchSize;
    
    @Value("${linger.ms:}")
    private String lingerMs;
    
    @Value("${buffer.memory:}")
    private String bufferMemory;
    
    private boolean isTest = false;
    
    private Producer<String, String> producer;

    private Properties getConfig() throws KafkaClientException {
        Properties props = new Properties();
        if (StringUtils.isEmpty(bootstrapServers)) {
            throw new InvalidConfigException("Kafka bootstrap servers not specified");
        } else {
            props.put("bootstrap.servers", bootstrapServers);
        }
        if (StringUtils.isEmpty(clientId)) {
            throw new InvalidConfigException("Kafka client ID not specified");
        } else {
            props.put("client.id", clientId);
        }
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        if (!StringUtils.isEmpty(acks)) {
            props.put("acks", acks);
        }
        if (!StringUtils.isEmpty(retries)) {
            props.put("retries", retries);
        }
        if (!StringUtils.isEmpty(batchSize)) {
            props.put("batch.size", batchSize);
        }
        if (!StringUtils.isEmpty(lingerMs)) {
            props.put("linger.ms", lingerMs);
        }
        if (!StringUtils.isEmpty(bufferMemory)) {
            props.put("buffer.memory", bufferMemory);
        }
        LOG.info("Conifiguring Kafka producer: " + props.toString());
        return props;
    }
    
    public SimpleStringProducer() {
        this.isTest = false;
    }

    public SimpleStringProducer(boolean isTest) {
        this.isTest = isTest;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isTest) {
            producer = new MockProducer<String, String>(Cluster.empty(), true, new DefaultPartitioner(), new StringSerializer(), new StringSerializer());
        } else {
            producer = new KafkaProducer<String, String>(getConfig());
        }
    }

    public <K, V> boolean send(String topic, K key, V value) {
        if (isTest) LOG.info("Sending [" + topic + "]  " + key + " : " + value);
        try {
            producer.send(new ProducerRecord<String, String>(topic, (String) key, (String) value)).get();
            return true;
        } catch (Exception e) {
            LOG.info ("ERROR: " + e.getMessage());
            return false;
        }
    }

    @Override
    public <K, V> void send(String topic, K key, V value,
            Callback cb) {
        if (isTest) LOG.info("Sending [" + topic + "]  " + key + " : " + value);
        producer.send(new ProducerRecord<String, String>(topic, (String) key, (String) value), cb);
    }

}

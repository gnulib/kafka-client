package com.cisco.collab.kafkaclient.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.cisco.collab.kafkaclient.common.ConsumerCallback;
import com.cisco.collab.kafkaclient.exceptions.IncompleteInitializationException;
import com.cisco.collab.kafkaclient.exceptions.InvalidConfigException;
import com.cisco.collab.kafkaclient.exceptions.InvalidOperationException;
import com.cisco.collab.kafkaclient.exceptions.KafkaClientException;

public class SimpleStringConsumerService implements ConsumerService, InitializingBean {
    private static Logger LOG = Logger.getLogger(SimpleStringConsumerService.class
            .getName());

    static final String serializer = "org.apache.kafka.common.serialization.StringSerializer";

    @Value("${bootstrap.servers}")
    private String bootstrapServers;

    @Value("${group.id}")
    private String groupId;

    private boolean isInitialized;
    
    private boolean isTest;

    // we are assuming symmetric implementation, and hence all running instances
    // of the cloud app will have exact same initialization, and will have same
    // topics registered. Therefore, no need to use a shared off the box
    // memcache for topic subscription management
    private Map<String, SimpleStringConsumer> consumersByTopic = new HashMap<String, SimpleStringConsumer>();

    private Properties getConfig() throws KafkaClientException {
        Properties props = new Properties();
        if (StringUtils.isEmpty(bootstrapServers)) {
            throw new InvalidConfigException(
                    "Kafka bootstrap servers not specified");
        } else {
            props.put("bootstrap.servers", bootstrapServers);
        }
        if (StringUtils.isEmpty(groupId)) {
            throw new InvalidConfigException("Kafka group ID not specified");
        } else {
            props.put("group.id", groupId);
        }
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        LOG.info("Conifiguring Kafka consumer: " + props.toString());
        return props;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!isTest) {
            LOG.info("Initializing Kafka consumer with config: " + getConfig());            
        }
        isInitialized = true;
    }

    public SimpleStringConsumerService() {
        isInitialized = false;
        isTest = false;
    }

    public SimpleStringConsumerService(Map<String, SimpleStringConsumer> consumersByTopic) {
        isInitialized = false;
        isTest = true;
        this.consumersByTopic = consumersByTopic;
    }

    @Override
    public void subscribe(String topic, ConsumerCallback cb, boolean autoCommit) {
        if (!isInitialized)
            throw new IncompleteInitializationException(
                    "subscribed before initialization");
        synchronized(this) {
            SimpleStringConsumer consumer = consumersByTopic.get(topic);
            if (consumer == null) {
                consumer = new SimpleStringConsumer(topic, getConfig());
                consumersByTopic.put(topic, consumer);
            }
            consumer.addCallback(cb, autoCommit);
        }
    }

    @Override
    public void unsubscribe(String topic, ConsumerCallback cb) {
        if (!isInitialized)
            throw new IncompleteInitializationException(
                    "unsubscribed before initialization");
        synchronized(this) {
            SimpleStringConsumer consumer = consumersByTopic.get(topic);
            if (consumer != null) {
                consumer.removeCallback(cb);
            } else {
                throw new InvalidOperationException("trying to unsubscribe from topic not subscribed already");
            }
        }
    }

    @Override
    public void commit(String topic) {
        if (!isInitialized)
            throw new IncompleteInitializationException(
                    "commit before initialization");
        synchronized(this) {
            SimpleStringConsumer consumer = consumersByTopic.get(topic);
            if (consumer != null) {
                consumer.commit(topic);
            } else {
                throw new InvalidOperationException("trying to commit on topic not subscribed already");
            }
        }
    }
}

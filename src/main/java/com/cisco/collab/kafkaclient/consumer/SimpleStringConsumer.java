package com.cisco.collab.kafkaclient.consumer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import com.cisco.collab.kafkaclient.common.ConsumerCallback;
import com.cisco.collab.kafkaclient.exceptions.UnsupportMethodException;

public class SimpleStringConsumer implements Runnable {
    private static Logger LOG = Logger.getLogger(SimpleStringConsumer.class
            .getName());

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Consumer<String, String> consumer;
    private String topic;
    private Thread thread;

    Set<ConsumerCallback> cbs = new HashSet<ConsumerCallback>();

    public Thread getThread() {
        return thread;
    }

    public SimpleStringConsumer(String topic, Properties props) {
        consumer = new KafkaConsumer<String, String>(props);
        this.topic = topic;
        consumer.subscribe(Arrays.asList(topic));
        thread = new Thread(this);
        thread.start();
    }

    public SimpleStringConsumer(String topic, Consumer<String, String> consumer) {
        this.consumer = consumer;
        this.topic = topic;
        consumer.subscribe(Arrays.asList(topic));
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        LOG.info("Starting consumer thread for topic: " + this.topic);
        try {
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                // notify all callbacks with records
                for (ConsumerCallback cb : cbs) {
                    // we may have to move the callback notification into a
                    // different thread and here just add the notification into
                    // a queue for that other thread to process so this thread
                    // execution will be very quick regardless of how long the
                    // notification processing takes by application
                    cb.consume(topic, records);
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get())
                throw e;
        } finally {
            consumer.close();
            LOG.info("Stopping consumer thread for topic: " + this.topic);
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }

    // add a subscriber callback
    public boolean addCallback(ConsumerCallback cb, boolean autoCommit) {
        if (!autoCommit)
            throw new UnsupportMethodException("explicit commit not supported");
        return cbs.add(cb);
    }

    // remove a subcriber callback
    public boolean removeCallback(ConsumerCallback cb) {
        return cbs.remove(cb);
    }

    public void commit(String topic) {
        throw new UnsupportMethodException("explicit commit not supported");        
    }
}

package com.cisco.collab.kafkaclient.producer;

import java.util.concurrent.ExecutionException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleStringProducerTest {

    public static final String TestTopic = "TestTopic";
    SimpleStringProducer producer = null;
    SendResult result = new SendResult();

    @BeforeMethod
    void init() throws Exception {
        producer = new SimpleStringProducer(true);
        producer.afterPropertiesSet();
        result.reset();
    }

    @Test
    public void testSendBlocking() throws InterruptedException, ExecutionException {
        Assert.assertTrue(producer.send(TestTopic, "test", "message"));
    }

    @Test
    public void testSendNonBlocking() throws InterruptedException,
            ExecutionException {
        producer.send(TestTopic, "test", "message", result);
        Assert.assertTrue(result.isDone());
    }
}

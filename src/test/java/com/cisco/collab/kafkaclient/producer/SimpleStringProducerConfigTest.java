package com.cisco.collab.kafkaclient.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(classes = { SimpleStringProducerTestConfig.class })
public class SimpleStringProducerConfigTest extends
        AbstractTestNGSpringContextTests {

    @Autowired
    SimpleStringProducer producer;

    public static final String TestTopic = "test";

    @Test
    public void testInitialization() {
        Assert.assertNotNull(producer);
    }

    @Test
    public void testRealSend() {
        Assert.assertTrue(producer.send(TestTopic, "test", "message"));
    }
}

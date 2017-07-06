package com.cisco.collab.kafkaclient.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(classes = { SimpleStringConsumerTestConfig.class })
public class SimpleStringConsumerConfigTest extends
AbstractTestNGSpringContextTests {

    @Autowired
    SimpleStringConsumerService consumer;

    public static final String TestTopic = "test";

    @Test
    public void testInitialization() {
        Assert.assertNotNull(consumer);
    }
}

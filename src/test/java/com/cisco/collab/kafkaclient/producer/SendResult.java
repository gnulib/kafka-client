package com.cisco.collab.kafkaclient.producer;

import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class SendResult implements Callback {
    private Logger LOG = Logger.getLogger(SendResult.class.getName());

    boolean done = false;
    
    @Override
    public void onCompletion(RecordMetadata arg0, Exception arg1) {
        if (arg0 != null) {
            LOG.info("###### SendResult Metadata: " + arg0.toString());
            done = true;
        }
        if (arg1 != null) {
            LOG.info("###### SendResult Exception: " + arg1.toString());
            done = false;
        }
    }

    public void reset() {
        done = false;
    }
    
    boolean isDone() {
        return done;
    }
}

package com.cisco.collab.kafkaclient.exceptions;

public class IncompleteInitializationException extends KafkaClientException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IncompleteInitializationException(String reason) {
        super(reason);
    }

}

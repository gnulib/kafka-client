package com.cisco.collab.kafkaclient.exceptions;

public class InvalidOperationException extends KafkaClientException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidOperationException(String reason) {
        super(reason);
    }

}

package com.cisco.collab.kafkaclient.exceptions;

public class UnsupportMethodException extends KafkaClientException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UnsupportMethodException(String reason) {
        super(reason);
    }

}

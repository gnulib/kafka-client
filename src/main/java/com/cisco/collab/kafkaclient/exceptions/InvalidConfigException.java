package com.cisco.collab.kafkaclient.exceptions;

public class InvalidConfigException extends KafkaClientException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidConfigException(String reason) {
        super(reason);
    }

}

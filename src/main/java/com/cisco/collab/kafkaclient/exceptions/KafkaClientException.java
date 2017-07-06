package com.cisco.collab.kafkaclient.exceptions;

public class KafkaClientException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String reason;

    public KafkaClientException() {
        super();
    }

    public KafkaClientException(String message) {
        super(message);
        this.reason = message;
    }

    public KafkaClientException(Throwable cause) {
        super(cause);
    }

    public KafkaClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaClientException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getReason() {
        return reason;
    }

    public String toString() {
        return reason;
    }

}
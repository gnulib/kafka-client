package com.cisco.collab.kafkaclient.consumer;

import com.cisco.collab.kafkaclient.common.ConsumerCallback;


public interface ConsumerService {
    
    void subscribe(String topic, ConsumerCallback cb, boolean autoCommit);
    
    void unsubscribe(String topic, ConsumerCallback cb);
    
    void commit(String topic);    
}

package com.yangzhenj.disruptor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendService {

    private static final Logger log = LoggerFactory.getLogger(SendService.class);

    public void sendMsg(String msg) {
        try {
            Thread.sleep(100L);
            log.info("send done. {}", msg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

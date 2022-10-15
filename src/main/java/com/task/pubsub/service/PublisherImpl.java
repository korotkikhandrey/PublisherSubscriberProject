package com.task.pubsub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PublisherImpl implements Publisher {

    @Autowired
    private PublisherSubscriberService publisherSubscriberService;

    @Override
    public void publish(String message) {
        publisherSubscriberService.addMessageToQueue();
    }
}

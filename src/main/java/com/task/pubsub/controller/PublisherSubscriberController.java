package com.task.pubsub.controller;

import com.task.pubsub.entity.Message;
import com.task.pubsub.service.PublisherSubscriberService;
import com.task.pubsub.service.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for publishing - subscribing process.
 */
@RestController
@RequestMapping("/api/v1")
public class PublisherSubscriberController {

    @Autowired
    private PublisherSubscriberService publisherSubscriberService;

    /**
     * Creates the subscriber.
     * @param subscriberName
     */
    @PostMapping("/addSubscriber")
    public void addSubscriber(@RequestBody String subscriberName){
        publisherSubscriberService.addSubscriber(subscriberName);
    }

    /**
     * Removes the subscriber
     * @param subscriber
     */
    @DeleteMapping ("/removeSubscriber")
    public void removeSubscriber(@RequestBody Subscriber subscriber){
        publisherSubscriberService.removeSubscriber(subscriber);
    }

    /**
     * Gets all the messages from DB.
     * @return
     */
    @GetMapping("/allMsgs")
    public List<Message> getAllMessages() {
        return publisherSubscriberService.getAllMessages();
    }

}

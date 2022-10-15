package com.task.pubsub.controller;

import com.task.pubsub.service.PublisherSubscriberService;
import com.task.pubsub.service.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PublisherSubscriberController {

    @Autowired
    private PublisherSubscriberService publisherSubscriberService;

    @PostMapping("/addSubscriber")
    public void addSubscriber(@RequestBody String subscriberName){
        publisherSubscriberService.addSubscriber(subscriberName);
    }

    @DeleteMapping ("/removeSubscriber")
    public void removeSubscriber(@RequestBody Subscriber subscriber){
        publisherSubscriberService.removeSubscriber(subscriber);
    }

}

package com.task.pubsub.controller;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import com.task.pubsub.service.PublisherSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/addSubscriber",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void addSubscriber(@RequestBody Subscriber subscriberName) {
        publisherSubscriberService.addSubscriber(subscriberName);
    }

    /**
     * Removes the subscriber by id
     * @param id
     */
    @DeleteMapping(value="/removeSubscriber/{id}")
    public void removeSubscriber(@PathVariable Long id) {
        publisherSubscriberService.removeSubscriber(id);
    }

    /**
     * Broadcast all the messages.
     */
    @GetMapping("/broadcastMessages")
    public void broadcastMessages() {
        publisherSubscriberService.broadcastMessages();
    }
    /**
     * Gets all the messages from DB.
     * @return
     */
    @GetMapping(value = "/getAllMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> getAllMessages() {
        return publisherSubscriberService.getAllMessagesFromDB();
    }

    @GetMapping(value = "/getAllMessagesForSubscriber/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> getAllMessagesForSubscriber(@PathVariable Long id) {
        return publisherSubscriberService.getAllMessagesForSubscriber(id);
    }
}

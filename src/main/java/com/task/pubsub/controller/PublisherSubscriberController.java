package com.task.pubsub.controller;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import com.task.pubsub.service.PublisherSubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for publishing - subscribing process.
 */
@RestController
@RequestMapping(value = "/pubsub")
public class PublisherSubscriberController {

    @Autowired
    private PublisherSubscriberService publisherSubscriberService;

    /**
     * Service for adding messages to the queue.
     * @param message
     */
    @PostMapping("/addMessageToQueue")
    public void addMessageToQueue(@RequestHeader(name = "apikey") String apikey,
                                  @RequestBody String message) {
        publisherSubscriberService.addMessageToQueue(message);
    }
    /**
     * Creates the subscriber.
     * @param subscriberName
     */
    @PostMapping(value = "/addSubscriber",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void addSubscriber(@RequestHeader(name = "apikey") String apikey,
                              @RequestBody Subscriber subscriberName) {
        publisherSubscriberService.addSubscriber(subscriberName);
    }

    /**
     * Removes the subscriber by id
     * @param id
     */
    @DeleteMapping(value="/removeSubscriber/{id}")
    public void removeSubscriber(@RequestHeader(name = "apikey") String apikey,
                                 @PathVariable Long id) {
        publisherSubscriberService.removeSubscriber(id);
    }

    /**
     * Broadcast all the messages.
     */
    @PostMapping("/broadcastMessages")
    public void broadcastMessages(@RequestHeader(name = "apikey") String apikey) {
        publisherSubscriberService.broadcastMessages();
    }
    /**
     * Gets all the messages from DB.
     * @return
     */
    @GetMapping(value = "/getAllMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> getAllMessages(@RequestHeader(name = "apikey") String apikey) {
        return publisherSubscriberService.getAllMessagesFromDB();
    }

    /**
     * Gets all the messages for the subscriber.
     * @param id
     * @return List of {@link Message}
     */
    @GetMapping(value = "/getAllMessagesForSubscriber/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Message> getAllMessagesForSubscriber(@RequestHeader(name = "apikey") String apikey,
                                                     @PathVariable Long id) {
        return publisherSubscriberService.getAllMessagesForSubscriber(id);
    }
}

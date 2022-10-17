package com.task.pubsub.controller;

import com.task.pubsub.entity.Message;
import com.task.pubsub.service.PublisherSubscriberService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.task.pubsub.utils.PubSubUtils.getMapResponseEntity;

/**
 * Rest controller for publishing - subscribing process.
 */
@RestController
@RequestMapping(value = "/pubsub")
public class PublisherSubscriberController {

    private final PublisherSubscriberService publisherSubscriberService;

    public PublisherSubscriberController(PublisherSubscriberService publisherSubscriberService) {
        this.publisherSubscriberService = publisherSubscriberService;
    }

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
                              @RequestBody String subscriberName) {
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
    public ResponseEntity<Map<String, Object>> getAllMessages(@RequestHeader(name = "apikey") String apikey,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              // should be used string like 'message,desc&createDate,asc&...' for sorting and direction
                                                              @RequestParam(defaultValue = "id,desc") String sort) {
        try {
            Page<Message> messagePage = publisherSubscriberService.getAllMessagesFromDB(page, size, sort);
            return getMapResponseEntity(messagePage);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all the messages for the subscriber.
     * @param id
     * @return List of {@link Message}
     */
    @GetMapping(value = "/getAllMessagesForSubscriber/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllMessagesForSubscriber(@RequestHeader(name = "apikey") String apikey,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size,
                                                     // should be used string like 'message,desc&createDate,asc&...' for sorting and direction
                                                     @RequestParam(defaultValue = "id,desc") String sort,
                                                     @PathVariable Long id) {

        try {
            Page<Message> messagePage = publisherSubscriberService.getAllMessagesForSubscriber(page, size, sort, id);
            return getMapResponseEntity(messagePage);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

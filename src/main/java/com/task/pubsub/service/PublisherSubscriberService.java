package com.task.pubsub.service;

import com.task.pubsub.entity.Message;
import com.task.pubsub.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Service for publishing-subscribing logic.
 */
@Service
public class PublisherSubscriberService {

    private List<String> messageQueue = new LinkedList<>();

    private Set<Subscriber> subscribers = new HashSet<>();

    @Autowired
    private MessageRepository messageRepository;

    private int counter = 0;

    /**
     * Adds message to the queue.
     */
    @Scheduled(fixedDelay = 5000)
    public void addMessageToQueue() {
        String messageStr = generateMessage();
        messageQueue.add(messageStr);

        //todo: save into db only after subscriber receives the msg, don't forget rempove the msg from the queue
        Message message = new Message();
        message.setMessage(messageStr);
        messageRepository.save(message);
    }

    private String generateMessage() {
        return "Some message " + ++counter;
    }

    public void addSubscriber(String name) {
        subscribers.add(new Subscriber(name));
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void broadcastMessage() {

    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}

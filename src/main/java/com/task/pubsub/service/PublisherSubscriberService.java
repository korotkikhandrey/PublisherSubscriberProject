package com.task.pubsub.service;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import com.task.pubsub.repository.MessageRepository;
import com.task.pubsub.repository.SubscriberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Service for publishing-subscribing logic.
 */
@Service
@Slf4j
public class PublisherSubscriberService {

    private Queue<String> messageQueue = new LinkedList<>();

    private Set<Subscriber> subscribers = new HashSet<>();

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    private int counter = 0;

    /**
     * Adds message to the queue.
     */
    @Scheduled(fixedDelay = 5000)
    public void addMessageToQueueScheduled() {
        String messageStr = generateMessage();
        messageQueue.add(messageStr);
    }

    /**
     * Adds message to the queue via endpoint
     * @param message
     */
    public void addMessageToQueue(String message) {
        messageQueue.add(message);
    }

    private String generateMessage() {
        return "Some message " + ++counter;
    }

    /**
     * Adds subscriber.
     * @param subscriber
     */
    public void addSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
        log.info("Subscriber with id {} has been created!", subscriber.getId());
    }

    /**
     * Removes subscriber by id.
     * @param id
     */
    public void removeSubscriber(Long id) {
        Optional<Subscriber> subscriberOpt = subscriberRepository.findById(id);
        if (subscriberOpt.isPresent()) {
            Subscriber subscriber = subscriberOpt.get();
            messageRepository.removeMessagesBySubscriber(subscriber);
            subscriberRepository.delete(subscriber);
            log.info("Subscriber with id {} has been deleted!", id);
        }
    }

    /**
     * Broadcasting all the messages from the queue.
     */
    public void broadcastMessages() {
        List<Subscriber> subscriberList = getAllSubscribers();
        if (CollectionUtils.isEmpty(subscriberList)) {
            log.info("There are no any subscribers!");
        } else {
            if (messageQueue.isEmpty()) {
                log.info("Queue is empty!");
            } else {
                int size = messageQueue.size();
                log.info("Messages with amount {} are about to be broadcasted...", size);
                while(!messageQueue.isEmpty()) {
                    handleMessageFromQueue(subscriberList);
                }
                log.info("Broadcast has been done for {} messages", size);
            }
        }

    }

    /**
     * Due to absence of topic message is assigned to particular subscriber in a random way.
     * @param subscriberList
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleMessageFromQueue(List<Subscriber> subscriberList) {
        String message = messageQueue.remove();
        Message msg = new Message();
        msg.setMessage(message);

        Subscriber subscriber =  getRandomSubscriber(subscriberList);
        msg.setSubscriber(subscriber);
        messageRepository.save(msg);
    }

    /**
     * Gets the random subscriber.
     * @return {@link Subscriber}
     */
    public Subscriber getRandomSubscriber(List<Subscriber> givenList) {
        Random rand = new Random();
        Subscriber randomSubscriber = null;
        int numberOfElements = givenList.size();

        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            randomSubscriber = givenList.get(randomIndex);
        }

        return randomSubscriber;
    }

    /**
     * Gets all the subscribers from DB
     * @return
     */
    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    /**
     * Gets all the Messages from DB.
     * @return
     */
    public List<Message> getAllMessagesFromDB() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessagesForSubscriber(Long id) {

        //todo: refactor it!!!!
        Optional<Subscriber> subscriber = subscriberRepository.findById(id);
        if (subscriber.isPresent()) {
            return messageRepository.findMessagesBySubscriber(subscriber.get());
        }

        return Collections.EMPTY_LIST;
    }
}

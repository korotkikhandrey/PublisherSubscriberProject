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
    public void addMessageToQueue() {
        String messageStr = generateMessage();
        messageQueue.add(messageStr);
    }

    private String generateMessage() {
        return "Some message " + ++counter;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
        log.info("Subscriber with id {} has been created!", subscriber.getId());
    }

    public void removeSubscriber(Long id) {
        Optional<Subscriber> subscriberOpt = subscriberRepository.findById(id);
        if (subscriberOpt.isPresent()) {
            Subscriber subscriber = subscriberOpt.get();
            //List<Message> messageList = messageRepository.findMessagesBySubscriber(subscriber);
            messageRepository.removeMessagesBySubscriber(subscriber);
            subscriberRepository.delete(subscriber);
            log.info("Subscriber with id {} has been deleted!", id);
        }
    }

    public void broadcastMessages() {
        List<Subscriber> subscriberList = getAllSubscribers();
        if (CollectionUtils.isEmpty(subscriberList)) {
            log.info("There are no any subscribers!");
        } else {
            if (messageQueue.isEmpty()) {
                System.out.println("Queue is empty!");
            } else {
                int size = messageQueue.size();

                while(!messageQueue.isEmpty()) {
                    handleMessageFromQueue(subscriberList);
                }
                log.info("Broadcast has been done for {} messages!", size);
            }
        }

    }

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
     * Due to topics absence the messages will be distributed randomly within the existing subscribers.
     *
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

package com.task.pubsub.utils;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;

import java.time.LocalDateTime;

/**
 * Utils class for test purposes.
 */
public class TestUtils {

    /**
     * Creates {@link Subscriber}
     * @param subscriberName
     * @return {@link Subscriber}
     */
    public static Subscriber createSubscriber(String subscriberName) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(subscriberName);
        return subscriber;
    }

    /**
     * Creates {@link Message}
     * @param id
     * @param message
     * @param subscriber
     * @param createDate
     * @return {@link Message}
     */
    public static Message createMessage(Long id,
                                        String message,
                                        Subscriber subscriber,
                                        LocalDateTime createDate) {
        Message messageObj = new Message();
        messageObj.setMessage(message);
        messageObj.setCreateDate(createDate);
        messageObj.setSubscriber(subscriber);
        messageObj.setId(id);
        return messageObj;
    }
}

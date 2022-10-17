package com.task.pubsub.service;

import com.task.pubsub.entity.Subscriber;
import com.task.pubsub.repository.MessageRepository;
import com.task.pubsub.repository.SubscriberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.task.pubsub.utils.TestUtils.createSubscriber;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link PublisherSubscriberService}
 */
@ExtendWith(SpringExtension.class)
public class PublisherSubscriberServiceTest {

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private SubscriberRepository subscriberRepository;
    @SpyBean
    private PublisherSubscriberService publisherSubscriberService;

    /**
     * Tests addMessageToQueueScheduled method.
     * Checks that message queue is not empty after 5 seconds.
     */
    @Test
    public void test_addMessageToQueueScheduled() {

        //when
        publisherSubscriberService.addMessageToQueueScheduled();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //then
        Assertions.assertTrue(publisherSubscriberService.getMessageQueue().size() > 0);
    }

    /**
     * Tests broadcastMessages method.
     * Verifies that message queue is empty after broadcasting.
     *
     */
    @Test
    public void test_broadcastMessages() {

        //given
        publisherSubscriberService.addMessageToQueueScheduled();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Subscriber> subscriberList = Arrays.asList(createSubscriber("Test subscriber"));
        when(subscriberRepository.findAll()).thenReturn(subscriberList);

        //when
        publisherSubscriberService.broadcastMessages();

        //then
        Assertions.assertTrue(publisherSubscriberService.getMessageQueue().size() == 0);
    }

    /**
     * Tests addMessageToQueue method.
     */
    @Test
    public void test_addMessageToQueue() {
        publisherSubscriberService.addMessageToQueue("msg");
        Assertions.assertTrue(publisherSubscriberService.getMessageQueue().size() > 0);
    }

    /**
     * Tests getAllMessagesFromDB method.
     * One-time call verification for messageRepository.
     */
    @Test
    public void test_getAllMessagesFromDB() throws Exception {
        publisherSubscriberService.getAllMessagesFromDB(1, 1, "id,desc");
        verify(messageRepository).findAll(any(Pageable.class));
    }

    /**
     * Tests getAllMessagesForSubscriber method.
     * One-time call verification for messageRepository.
     */
    @Test
    public void test_getAllMessagesForSubscriber() throws Exception {

        //given
        Optional<Subscriber> subscriberOpt = Optional.of(createSubscriber("Test subscriber"));
        when(subscriberRepository.findById(anyLong())).thenReturn(subscriberOpt);

        //when
        publisherSubscriberService.getAllMessagesForSubscriber(1, 1, "id,desc", 1L);

        //then
        verify(messageRepository).findMessagesBySubscriber(any(Pageable.class), any(Subscriber.class));
    }

    /**
     * Tests addSubscriber method.
     * One-time call verification for subscriberRepository.
     */
    @Test
    public void test_addSubscriber() {
        publisherSubscriberService.addSubscriber("Subscr");
        verify(subscriberRepository).save(any(Subscriber.class));
    }

    /**
     * Tests removeSubscriber method.
     * One-time calls verifications for messageRepository and subscriberRepository.
     */
    @Test
    public void test_removeSubscriber() {

        //given
        Optional<Subscriber> subscriberOpt = Optional.of(createSubscriber("Test subscriber"));
        when(subscriberRepository.findById(any(Long.class))).thenReturn(subscriberOpt);

        //when
        publisherSubscriberService.removeSubscriber(1L);

        //then
        verify(messageRepository).removeMessagesBySubscriber(any(Subscriber.class));
        verify(subscriberRepository).delete(any(Subscriber.class));
    }
}

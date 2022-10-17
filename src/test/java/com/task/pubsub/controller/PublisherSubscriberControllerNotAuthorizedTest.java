package com.task.pubsub.controller;

import com.task.pubsub.repository.MessageRepository;
import com.task.pubsub.repository.SubscriberRepository;
import com.task.pubsub.service.PublisherSubscriberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for {@link PublisherSubscriberController}
 */
@WebMvcTest
public class PublisherSubscriberControllerNotAuthorizedTest {

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private SubscriberRepository subscriberRepository;

    @MockBean
    private PublisherSubscriberService publisherSubscriberService;

    @SpyBean
    private PublisherSubscriberController publisherSubscriberController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_getAllMessages_nonAuthorized() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/pubsub/getAllMessages")
                .accept(MediaType.ALL))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }
}

package com.task.pubsub.controller;

import com.task.pubsub.repository.MessageRepository;
import com.task.pubsub.repository.SubscriberRepository;
import com.task.pubsub.security.APIKeyAuthFilter;
import com.task.pubsub.security.APISecurityConfig;
import com.task.pubsub.service.PublisherSubscriberService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Test for {@link PublisherSubscriberController}
 */
@ActiveProfiles(value = "test")
@WebMvcTest(PublisherSubscriberController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
public class PublisherSubscriberControllerTest {

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private SubscriberRepository subscriberRepository;

    @MockBean
    private PublisherSubscriberService publisherSubscriberService;

    @SpyBean
    private PublisherSubscriberController publisherSubscriberController;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SecurityFilterChain filterChain;

    @SpyBean
    APISecurityConfig apiSecurityConfig;

    /*@Autowired
    APIKeyAuthFilter apiKeyAuthFilter;*/

    /*@Before("")
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }*/


    @Test
    public void test_getAllMessages_nonAuthorized() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/pubsub/getAllMessages")
                .accept(MediaType.ALL))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    public void test_getAllMessages_authorized() throws Exception {


        /*mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();*/
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/pubsub/getAllMessages")
                        .header("apikey", "apikey_token")
                        .accept(MediaType.ALL))

                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}

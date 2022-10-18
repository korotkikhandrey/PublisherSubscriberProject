package com.task.pubsub.controller;

import com.task.pubsub.entity.Message;
import com.task.pubsub.repository.MessageRepository;
import com.task.pubsub.repository.SubscriberRepository;
import com.task.pubsub.security.APISecurityConfig;
import com.task.pubsub.service.PublisherSubscriberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.task.pubsub.utils.TestUtils.createMessage;
import static com.task.pubsub.utils.TestUtils.createSubscriber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Test for {@link PublisherSubscriberController}
 */
@ActiveProfiles(value = "test")
@WebMvcTest(PublisherSubscriberController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
public class PublisherSubscriberControllerAuthorizedTest {

    @MockBean
    private PublisherSubscriberService publisherSubscriberService;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that in case of there are some messages in DB, they will be returned with code 200.
     * @throws Exception
     */
    @Test
    public void test_getAllMessages_authorized_OK() throws Exception {

        //given
        List<Message> messageList = Arrays.asList(
                createMessage(1L, "message1", createSubscriber("Subscriber1"), LocalDateTime.now()),
                createMessage(2L, "message2", createSubscriber("Subscriber2"), LocalDateTime.now()),
                createMessage(3L, "message3", createSubscriber("Subscriber3"), LocalDateTime.now())
        );

        Page<Message> messagePage = new PageImpl<>(messageList);
        when(publisherSubscriberService.getAllMessagesFromDB(anyInt(), anyInt(), anyString())).thenReturn(messagePage);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/pubsub/getAllMessages")
                .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * Tests that in case of NullPointerException, code 500 will be returned.
     * @throws Exception
     */
    @Test
    public void test_getAllMessages_authorized_NOK() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/pubsub/getAllMessages")
                .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    /**
     * Tests /pubsub/addMessageToQueue endpoint, code 200 is returned
     * @throws Exception
     */
    @Test
    public void test_addMessageToQueue_authorized_OK() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/pubsub/addMessageToQueue").content("msg")
                .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * Tests /pubsub/addSubscriber endpoint, code 200 is returned
     * @throws Exception
     */
    @Test
    public void test_addSubscriber_authorized_OK() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/pubsub/addSubscriber").content("Subscriber1")
                .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * Tests /pubsub/broadcastMessages endpoint, code 200 is returned
     * @throws Exception
     */
    @Test
    public void test_broadcastMessages_authorized_OK() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/pubsub/broadcastMessages")
                        .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * Tests /pubsub/removeSubscriber endpoint, code 200 is returned
     * @throws Exception
     */
    @Test
    public void test_removeSubscriber_authorized_OK() throws Exception {

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .delete("/pubsub/removeSubscriber/1")
                .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * Tests /pubsub/getAllMessagesForSubscriber endpoint, code 200 is returned
     * @throws Exception
     */
    @Test
    public void test_getAllMessagesForSubscriber_authorized_OK() throws Exception {

        //given
        List<Message> messageList = Arrays.asList(
                createMessage(1L, "message1", createSubscriber("Subscriber1"), LocalDateTime.now()),
                createMessage(2L, "message2", createSubscriber("Subscriber2"), LocalDateTime.now()),
                createMessage(3L, "message3", createSubscriber("Subscriber3"), LocalDateTime.now())
        );

        Page<Message> messagePage = new PageImpl<>(messageList);
        when(publisherSubscriberService.getAllMessagesForSubscriber(anyInt(), anyInt(), anyString(), anyLong())).thenReturn(messagePage);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/pubsub/getAllMessagesForSubscriber/1")
                        .header("apikey", "apikey_token"))
                .andReturn();

        //then
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }
}

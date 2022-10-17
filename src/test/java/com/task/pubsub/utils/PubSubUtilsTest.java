package com.task.pubsub.utils;

import com.task.pubsub.entity.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.task.pubsub.utils.TestUtils.createMessage;
import static com.task.pubsub.utils.TestUtils.createSubscriber;

/**
 * Test class for {@link PubSubUtils}
 */
@ExtendWith(SpringExtension.class)
public class PubSubUtilsTest {

    /**
     * Tests case when sort param has one column for sorting: "id,desc"
     */
    @Test
    public void test_getPagingSort_oneSortParam() {

        //given
        String sort = "id,desc";
        int page = 1;
        int size = 20;

        //when
        Pageable pageable = PubSubUtils.getPagingSort(page, size, sort);

        //then
        Assertions.assertTrue(pageable != null);
        Assertions.assertTrue(pageable.getPageNumber() == page);
        Assertions.assertTrue(pageable.getPageSize() == size);
        Sort sortObj = pageable.getSort();
        Assertions.assertNotNull(sortObj);
        Assertions.assertNotNull(sortObj.stream().iterator().next());
        Sort.Order order = sortObj.getOrderFor("id");
        Assertions.assertEquals("id", order.getProperty());
        Assertions.assertEquals(Sort.Direction.DESC, order.getDirection());
    }

    /**
     * Tests case when sort param has several columns for sorting: "id,desc&createDate,asc"
     */
    @Test
    public void test_getPagingSort_twoSortParams() {

        //given
        String sort = "id,desc&createDate,asc";
        int page = 1;
        int size = 20;

        //when
        Pageable pageable = PubSubUtils.getPagingSort(page, size, sort);


        //then
        Assertions.assertTrue(pageable != null);
        Assertions.assertTrue(pageable.getPageNumber() == page);
        Assertions.assertTrue(pageable.getPageSize() == size);

        Sort sortObj = pageable.getSort();
        Assertions.assertNotNull(sortObj);

        Sort.Order orderID = sortObj.getOrderFor("id");
        Assertions.assertEquals("id", orderID.getProperty());
        Assertions.assertEquals(Sort.Direction.DESC, orderID.getDirection());

        Sort.Order orderCreateDate = sortObj.getOrderFor("createDate");
        Assertions.assertEquals("createDate", orderCreateDate.getProperty());
        Assertions.assertEquals(Sort.Direction.ASC, orderCreateDate.getDirection());
    }

    /**
     * Tests that for incoming Page<Message> object with a list of {@link Message} in it
     * method gives Map wrapped into {@link ResponseEntity} with current page, total pages, messages data;
     */
    @Test
    public void test_getMapResponseEntity() {
        List<Message> messageList = Arrays.asList(
            createMessage(1L, "message1", createSubscriber("Subscriber1"), LocalDateTime.now()),
            createMessage(2L, "message2", createSubscriber("Subscriber2"), LocalDateTime.now()),
            createMessage(3L, "message3", createSubscriber("Subscriber3"), LocalDateTime.now())
        );

        Page<Message> messagePage = new PageImpl<>(messageList);
        ResponseEntity<Map<String, Object>> mapResponseEntity = PubSubUtils.getMapResponseEntity(messagePage);

        Assertions.assertNotNull(mapResponseEntity);
        Map<String, Object> mapResponse = mapResponseEntity.getBody();
        Assertions.assertEquals(Long.valueOf(3L), mapResponse.get("totalItems"));
        Assertions.assertEquals(Integer.valueOf(1), mapResponse.get("totalPages"));
        Assertions.assertTrue(((Collection) mapResponse.get("messages")).size() == 3);
        Assertions.assertEquals(Integer.valueOf(0), mapResponse.get("currentPage"));
    }

}

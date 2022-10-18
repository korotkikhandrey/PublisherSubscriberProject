package com.task.pubsub.utils;

import com.task.pubsub.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils class
 */
public class PubSubUtils {

    /**
     * Gets {@link Pageable} object for given page, size and sort string.
     * @param page
     * @param size
     * @param sortStr
     * @return {@link Pageable}
     */
    public static Pageable getPagingSort(int page, int size, String sortStr) {
        if (StringUtils.isEmpty(sortStr) || size == 0) {
            return  PageRequest.of(page, size);
        }
        String[] sort = sortStr.split("&");
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

    /**
     * Gets {@link Sort.Direction} for given string: asc or desc.
     * @param direction
     * @return {@link Sort.Direction}
     */
    private static Sort.Direction getSortDirection(String direction) {
        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    /**
     * Creates ResponseEntity for paged and sorted messages.
     * @param messagePage
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<Map<String, Object>> getMapResponseEntity(Page<Message> messagePage) {
        Map<String, Object> response = new HashMap<>();
        List<Message> messageList = messagePage.getContent();
        if (!messageList.isEmpty()) {
            response.put("messages", messagePage.getContent());
            response.put("currentPage", messagePage.getNumber());
            response.put("totalItems", messagePage.getTotalElements());
            response.put("totalPages", messagePage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}

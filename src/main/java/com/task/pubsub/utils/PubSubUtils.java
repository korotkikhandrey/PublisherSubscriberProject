package com.task.pubsub.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

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

        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        return pagingSort;
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

}

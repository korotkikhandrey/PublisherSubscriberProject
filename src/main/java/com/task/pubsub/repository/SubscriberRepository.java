package com.task.pubsub.repository;

import com.task.pubsub.entity.Subscriber;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repo for saving messages into MESSAGE table.
 */
public interface SubscriberRepository extends PagingAndSortingRepository<Subscriber, Long> {
}

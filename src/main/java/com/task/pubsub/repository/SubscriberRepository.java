package com.task.pubsub.repository;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repo for saving messages into MESSAGE table.
 */
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
}

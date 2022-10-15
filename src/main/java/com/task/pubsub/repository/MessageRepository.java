package com.task.pubsub.repository;

import com.task.pubsub.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repo for saving messages into MESSAGE table.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
}

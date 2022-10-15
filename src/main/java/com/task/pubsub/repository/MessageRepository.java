package com.task.pubsub.repository;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repo for saving messages into MESSAGE table.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySubscriber(Subscriber subscriber);

    @Modifying
    @Query("delete from Message m where m.subscriber = :subscriber")
    @Transactional
    void removeMessagesBySubscriber(@Param("subscriber") Subscriber subscriber);

}

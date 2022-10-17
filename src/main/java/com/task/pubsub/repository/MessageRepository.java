package com.task.pubsub.repository;

import com.task.pubsub.entity.Message;
import com.task.pubsub.entity.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repo for saving messages into MESSAGE table.
 */
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {

    /**
     * Gets all messages for subscriber
     * @param subscriber
     * @return List of {@link Message}
     */
    Page<Message> findMessagesBySubscriber(Pageable pageable, Subscriber subscriber);

    /**
     * Deletes all messages for particular subscriber.
     * @param subscriber
     */
    @Modifying
    @Query("delete from Message m where m.subscriber = :subscriber")
    @Transactional
    void removeMessagesBySubscriber(@Param("subscriber") Subscriber subscriber);

}

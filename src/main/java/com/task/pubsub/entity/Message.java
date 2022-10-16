package com.task.pubsub.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.*;



import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Message entity class.
 */
@Entity
@Table(name = "message")
@Getter
@Setter
@Data
@NoArgsConstructor
public class Message extends AbstractEntity {

    @NotNull
    @Column(name="message")
    private String message;


    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(subscriber, message1.subscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, subscriber);
    }

}

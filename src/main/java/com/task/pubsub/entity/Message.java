package com.task.pubsub.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", subscriber=" + subscriber +
                '}';
    }
}

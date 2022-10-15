package com.task.pubsub.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.*;



import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

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


}

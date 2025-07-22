package com.cloudhumans.smartchat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionRetrieved extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "score", nullable = false)
    private float score;

    @Column(name = "content", nullable = false)
    private String content;
}

package com.cloudhumans.smartchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "retrieved_section", nullable = false)
    private boolean retrievedSection;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message")
    private Set<SectionRetrieved> sectionRetrieveds = new HashSet<>();
}

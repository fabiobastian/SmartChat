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
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    @Column(name = "helpdesk_id", nullable = false)
    private int helpdeskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "handover_to_human_needed", nullable = false)
    private boolean handoverToHumanNeeded;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createAt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    private Set<Message> messages = new HashSet<>();
}

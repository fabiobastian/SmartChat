package com.cloudhumans.smartchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<Conversation> conversation = new HashSet<>();
}

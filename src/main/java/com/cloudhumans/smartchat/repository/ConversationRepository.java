package com.cloudhumans.smartchat.repository;

import com.cloudhumans.smartchat.entity.Conversation;
import com.cloudhumans.smartchat.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findConversationByProjectAndHelpdeskId(Project project, Integer integer);
}

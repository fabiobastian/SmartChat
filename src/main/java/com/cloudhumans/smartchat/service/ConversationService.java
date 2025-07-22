package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.dto.ConversationRequest;
import com.cloudhumans.smartchat.dto.ConversationResponse;
import com.cloudhumans.smartchat.dto.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.EmbeddingResponse;
import com.cloudhumans.smartchat.entity.Conversation;
import com.cloudhumans.smartchat.entity.Project;
import com.cloudhumans.smartchat.repository.ConversationRepository;
import com.cloudhumans.smartchat.repository.MessageRepository;
import com.cloudhumans.smartchat.repository.ProjectRepository;
import com.cloudhumans.smartchat.repository.SectionRetrievedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RequiredArgsConstructor
@Service
public class ConversationService {

    public static final String MODEL_EMBEDDING_3_LARGE = "text-embedding-3-large";

    private final EmbeddingService embeddingService;
    private final SearchService searchService;
    private final ChatService chatService;

    private final ProjectRepository projectRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SectionRetrievedRepository sectionRetrievedRepository;

    public ConversationResponse getChatComplemention(ConversationRequest request) {

        Project project = projectRepository.findProjectByName(request.projectName())
                .orElseThrow(() -> new ResponseStatusException(
                UNPROCESSABLE_ENTITY, "Project not found"
        ));

        Conversation conversation = new Conversation();
        conversation.setProject(project);
        conversation.setHelpdeskId(request.helpdeskId());
        conversationRepository.save(conversation);

        /*
        EmbeddingRequest embeddingRequest =
                new EmbeddingRequest(MODEL_EMBEDDING_3_LARGE, request.messages().get(0).content());

        EmbeddingResponse embeddingResponse = embeddingService.generateEmbedding(embeddingRequest);



         */

        return null;
    }
}

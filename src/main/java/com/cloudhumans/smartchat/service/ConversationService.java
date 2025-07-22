package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.dto.ConversationRequest;
import com.cloudhumans.smartchat.dto.ConversationResponse;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final EmbeddingService embeddingService;
    private final SearchService searchService;
    private final ChatService chatService;

    public ConversationService(EmbeddingService embeddingService, SearchService searchService, ChatService chatService) {
        this.embeddingService = embeddingService;
        this.searchService = searchService;
        this.chatService = chatService;
    }

    public ConversationResponse getChatComplemention(ConversationRequest request) {
        return null;
    }
}

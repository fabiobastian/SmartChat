package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.dto.chat.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.chat.ChatCompletionResponse;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingResponse;
import com.cloudhumans.smartchat.dto.search.SearchRequest;
import com.cloudhumans.smartchat.dto.search.SearchResponse;
import com.cloudhumans.smartchat.service.ChatService;
import com.cloudhumans.smartchat.service.EmbeddingService;
import com.cloudhumans.smartchat.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RequiredArgsConstructor
@RestController
@RequestMapping("/manual-tests")
public class ManualTestController {

    private final EmbeddingService embeddingService;
    private final SearchService searchService;
    private final ChatService chatService;

    @PostMapping("/embeddings")
    public EmbeddingResponse getEmbeddings(@RequestBody EmbeddingRequest request) {
        return embeddingService.generateEmbedding(request);
    }

    @PostMapping("/questions")
    public SearchResponse getQuestions(@RequestBody SearchRequest searchRequest) {
        return searchService.searchRelevantQuestions(searchRequest);
    }

    @PostMapping("/chat-completions")
    public ChatCompletionResponse getAnswer(@RequestBody ChatCompletionRequest request) {
        return chatService.generateResponse(request);
    }
}

package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.dto.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.EmbeddingResponse;
import com.cloudhumans.smartchat.service.EmbeddingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("embeddings")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping
    public EmbeddingResponse getEmbeddings(@RequestBody EmbeddingRequest request) {
        return embeddingService.generateEmbedding(request);
    }
}

package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIEmbeddingClient;
import com.cloudhumans.smartchat.dto.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.EmbeddingResponse;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private final OpenAIEmbeddingClient openAIEmbeddingClient;

    public EmbeddingService(OpenAIEmbeddingClient openAIEmbeddingClient) {
        this.openAIEmbeddingClient = openAIEmbeddingClient;
    }

    public EmbeddingResponse generateEmbedding(EmbeddingRequest request) {
        return openAIEmbeddingClient.getEmbedding(request);
    }
}

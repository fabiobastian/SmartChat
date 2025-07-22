package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIEmbeddingClient;
import com.cloudhumans.smartchat.dto.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private final OpenAIEmbeddingClient openAIEmbeddingClient;

    public EmbeddingResponse generateEmbedding(EmbeddingRequest request) {
        return openAIEmbeddingClient.getEmbedding(request);
    }
}

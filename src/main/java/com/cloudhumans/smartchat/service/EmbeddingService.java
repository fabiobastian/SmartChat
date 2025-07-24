package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIEmbeddingClient;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class EmbeddingService {

    private static final String ERROR_FEIGN_PROCESS_OPENAI = "Failed to process OpenAI embedding request";

    private final OpenAIEmbeddingClient openAIEmbeddingClient;

    public EmbeddingResponse generateEmbedding(EmbeddingRequest request) {
        try {
            return openAIEmbeddingClient.getEmbedding(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, ERROR_FEIGN_PROCESS_OPENAI, e);
        }
    }
}

package com.cloudhumans.smartchat.client;

import com.cloudhumans.smartchat.config.OpenAIClientConfig;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openai-embeddings",
        url = "${feigh.openai.embedding.url}",
        configuration = OpenAIClientConfig.class)
public interface OpenAIEmbeddingClient {

    @PostMapping(value = "/embeddings", consumes = MediaType.APPLICATION_JSON_VALUE)
    EmbeddingResponse getEmbedding(@RequestBody EmbeddingRequest request);
}

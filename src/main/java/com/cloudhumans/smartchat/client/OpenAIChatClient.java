package com.cloudhumans.smartchat.client;

import com.cloudhumans.smartchat.config.OpenAIClientConfig;
import com.cloudhumans.smartchat.dto.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.ChatCompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openAIChatClient",
        url = "${feigh.openai.chat.url}",
        configuration = OpenAIClientConfig.class)
public interface OpenAIChatClient {

    @PostMapping("/chat/completions")
    ChatCompletionResponse createChatCompletion(@RequestBody ChatCompletionRequest request);
}

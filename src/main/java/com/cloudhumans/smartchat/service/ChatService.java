package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIChatClient;
import com.cloudhumans.smartchat.dto.chat.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.chat.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private static final String ERROR_FEIGN_PROCESS_OPENAI = "Failed to process OpenAI completion request";

    private final OpenAIChatClient openAIChatClient;

    public ChatCompletionResponse generateResponse(ChatCompletionRequest request) {
        try {
            return openAIChatClient.createChatCompletion(request);
        } catch (Exception e) {
            throw new RuntimeException(ERROR_FEIGN_PROCESS_OPENAI);
        }
    }
}

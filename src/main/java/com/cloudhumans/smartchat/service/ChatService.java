package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIChatClient;
import com.cloudhumans.smartchat.dto.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final OpenAIChatClient openAIChatClient;

    public ChatCompletionResponse getChatAnswer(ChatCompletionRequest request) {
        try {
            return openAIChatClient.createChatCompletion(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process OpenAI completion request");
        }
    }
}

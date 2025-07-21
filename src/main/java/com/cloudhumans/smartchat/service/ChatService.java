package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.OpenAIChatClient;
import com.cloudhumans.smartchat.dto.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.ChatCompletionResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAIChatClient openAIChatClient;

    public ChatService(OpenAIChatClient openAIChatClient) {
        this.openAIChatClient = openAIChatClient;
    }

    public ChatCompletionResponse getChatAnswer(ChatCompletionRequest request) {
        return openAIChatClient.createChatCompletion(request);
    }
}

package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.dto.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.ChatCompletionResponse;
import com.cloudhumans.smartchat.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatCompletionResponse getAnswer(@RequestBody ChatCompletionRequest request) {
        return chatService.getChatAnswer(request);
    }
}

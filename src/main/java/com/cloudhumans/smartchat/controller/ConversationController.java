package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.dto.ConversationRequest;
import com.cloudhumans.smartchat.dto.ConversationResponse;
import com.cloudhumans.smartchat.service.ConversationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("completions")
    public ConversationResponse conversationComplemention(@RequestBody ConversationRequest request) {
        return conversationService.getChatComplemention(request);
    }
}

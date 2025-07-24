package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.controller.docs.ConversationControllerDoc;
import com.cloudhumans.smartchat.dto.conversation.ConversationRequest;
import com.cloudhumans.smartchat.dto.conversation.ConversationResponse;
import com.cloudhumans.smartchat.service.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("conversations")
public class ConversationController implements ConversationControllerDoc {

    private final ConversationService conversationService;

    @PostMapping("completions")
    @Override
    public ConversationResponse createCompletion(@Valid @RequestBody ConversationRequest request) {
        return conversationService.generateCompletion(request);
    }
}

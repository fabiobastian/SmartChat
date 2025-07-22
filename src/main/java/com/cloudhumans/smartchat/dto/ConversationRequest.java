package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ConversationRequest(
        String helpdeskId,
        String projectName,
        List<Message> messages
) {
}

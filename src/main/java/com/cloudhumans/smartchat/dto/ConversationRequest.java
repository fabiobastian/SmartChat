package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ConversationRequest(
        Integer helpdeskId,
        String projectName,
        List<MessageDTO> messages
) {
}

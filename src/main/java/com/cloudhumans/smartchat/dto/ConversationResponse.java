package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ConversationResponse(
        boolean handoverToHumanNeeded,
        List<MessageDTO> messages,
        List<SearchResultItem> sectionsRetrieved
) {
}

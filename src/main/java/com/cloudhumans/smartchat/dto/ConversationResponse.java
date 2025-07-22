package com.cloudhumans.smartchat.dto;

import java.util.List;

public record ConversationResponse(
        boolean handoverToHumanNeeded,
        List<SearchResultItem> sectionsRetrieved,
        List<Message> messages
) {
}

package com.cloudhumans.smartchat.dto.conversation;

import com.cloudhumans.smartchat.dto.search.SearchResultItem;
import com.cloudhumans.smartchat.dto.chat.MessageDTO;

import java.util.List;

public record ConversationResponse(
        boolean handoverToHumanNeeded,
        List<MessageDTO> messages,
        List<SearchResultItem> sectionsRetrieved
) {
}

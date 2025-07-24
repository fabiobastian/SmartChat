package com.cloudhumans.smartchat.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(
        int index,
        MessageDTO message,
        @JsonProperty("finish_reason")
        String finishReason
) {}
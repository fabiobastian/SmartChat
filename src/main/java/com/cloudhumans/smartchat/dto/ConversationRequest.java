package com.cloudhumans.smartchat.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record ConversationRequest(
        @Min(value = 1)
        Integer helpdeskId,
        @NotBlank
        @Size(min = 4, max = 255)
        String projectName,
        @NotEmpty(message = "The list of questions cannot be empty")
        List<MessageDTO> messages
) {
}

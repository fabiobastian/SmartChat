package com.cloudhumans.smartchat.dto.chat;

import com.cloudhumans.smartchat.entity.Role;
import jakarta.validation.constraints.Size;

public record MessageDTO(
        Role role,
        @Size(min = 2, max = 500)
        String content
) {}
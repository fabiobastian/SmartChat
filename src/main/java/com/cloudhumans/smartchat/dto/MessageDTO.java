package com.cloudhumans.smartchat.dto;

import com.cloudhumans.smartchat.entity.Role;
import jakarta.validation.constraints.Size;

public record MessageDTO(
        Role role,
        @Size(min = 2, max = 500)
        String content
) {}
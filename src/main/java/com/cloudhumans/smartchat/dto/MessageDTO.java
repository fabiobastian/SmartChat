package com.cloudhumans.smartchat.dto;

import com.cloudhumans.smartchat.entity.Role;

public record MessageDTO(
        Role role,
        String content
) {}
package com.cloudhumans.smartchat.dto;

public record Message(
        String role,
        String content
) {}
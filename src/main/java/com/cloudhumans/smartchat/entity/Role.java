package com.cloudhumans.smartchat.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
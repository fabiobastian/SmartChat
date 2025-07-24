package com.cloudhumans.smartchat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SearchResultItem(
        @JsonProperty("@search.score")
        float searchScore,
        String content,
        String type
) {}

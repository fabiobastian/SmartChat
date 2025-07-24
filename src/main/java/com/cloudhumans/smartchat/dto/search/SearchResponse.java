package com.cloudhumans.smartchat.dto.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchResponse(
        @JsonProperty("@odata.context")
        String odataContext,
        @JsonProperty("@odata.count")
        int odataCount,
        List<SearchResultItem> value
) {}

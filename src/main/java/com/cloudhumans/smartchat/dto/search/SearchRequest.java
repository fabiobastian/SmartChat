package com.cloudhumans.smartchat.dto.search;

import com.cloudhumans.smartchat.dto.embedding.VectorQuery;

import java.util.List;

public record SearchRequest(
        boolean count,
        String select,
        int top,
        String filter,
        List<VectorQuery> vectorQueries
) {
}

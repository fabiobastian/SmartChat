package com.cloudhumans.smartchat.dto;

import java.util.List;

public record SearchRequest(
        boolean count,
        String select,
        int top,
        String filter,
        List<VectorQuery> vectorQueries
) {
}

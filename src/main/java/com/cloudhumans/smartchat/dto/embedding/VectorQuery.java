package com.cloudhumans.smartchat.dto.embedding;

import java.util.List;

public record VectorQuery(
        List<Float> vector,
        int k,
        String fields,
        String kind
) {
}

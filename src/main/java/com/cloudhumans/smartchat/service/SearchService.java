package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.AzureSearchClient;
import com.cloudhumans.smartchat.dto.search.SearchRequest;
import com.cloudhumans.smartchat.dto.search.SearchResponse;
import com.cloudhumans.smartchat.dto.embedding.VectorQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private static final String ERROR_FEIGN_PROCESS_AZURE = "Failed to process Azure search request";
    private static final int TOP_K = 3;
    private static final String VECTOR_FIELD_EMBEDDINGS = "embeddings";
    private static final String VECTOR_SEARCH_KIND = "vector";
    private static final boolean INCLUDE_TOTAL_COUNT = true;
    private static final String SEARCH_FIELDS = "content, type";
    private static final int TOP_K_RESULTS = 10;
    private static final String FILTER_TEMPLATE = "projectName eq '%s'";

    private final AzureSearchClient azureSearchClient;

    public SearchResponse searchRelevantQuestions(SearchRequest searchRequest) {
        try {
            return azureSearchClient.search(searchRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, ERROR_FEIGN_PROCESS_AZURE, e);
        }
    }

    public SearchResponse searchByEmbedding(List<Float> embedding, String projectName) {
        SearchRequest request = buildSearchRequest(embedding, projectName);
        return searchRelevantQuestions(request);
    }

    private SearchRequest buildSearchRequest(List<Float> embedding, String projectName) {
        List<VectorQuery> vectorQueries = List.of(
                new VectorQuery(embedding, TOP_K, VECTOR_FIELD_EMBEDDINGS, VECTOR_SEARCH_KIND)
        );

        String filter = String.format(FILTER_TEMPLATE, projectName);

        return new SearchRequest(INCLUDE_TOTAL_COUNT, SEARCH_FIELDS, TOP_K_RESULTS, filter, vectorQueries);
    }
}

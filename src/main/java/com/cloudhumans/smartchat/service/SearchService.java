package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.AzureSearchClient;
import com.cloudhumans.smartchat.dto.SearchRequest;
import com.cloudhumans.smartchat.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final AzureSearchClient azureSearchClient;

    public SearchResponse searchRelevantQuestions(SearchRequest searchRequest) {
        try {
            return azureSearchClient.search(searchRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Azure search request");
        }
    }
}

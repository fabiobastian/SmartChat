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
        return azureSearchClient.search(searchRequest);
    }

    public SearchResponse searchRelevantQuestions2(SearchRequest searchRequest) {
        return searchRelevantQuestions(searchRequest);
    }
}

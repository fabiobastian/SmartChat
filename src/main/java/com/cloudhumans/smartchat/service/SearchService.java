package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.client.AzureSearchClient;
import com.cloudhumans.smartchat.dto.SearchRequest;
import com.cloudhumans.smartchat.dto.SearchResponse;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final AzureSearchClient azureSearchClient;

    public SearchService(AzureSearchClient azureSearchClient) {
        this.azureSearchClient = azureSearchClient;
    }

    public SearchResponse searchRelevantQuestions(SearchRequest searchRequest) {
        return azureSearchClient.search(searchRequest);
    }
}

package com.cloudhumans.smartchat.client;

import com.cloudhumans.smartchat.config.AzureSearchClientConfig;
import com.cloudhumans.smartchat.dto.search.SearchRequest;
import com.cloudhumans.smartchat.dto.search.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "azureSearchClient",
        url = "${feigh.azure.search.url}",
        configuration = AzureSearchClientConfig.class)
public interface AzureSearchClient {

    @PostMapping("/indexes/claudia-ids-index-large/docs/search?api-version=2023-11-01")
    SearchResponse search(@RequestBody SearchRequest request);
}
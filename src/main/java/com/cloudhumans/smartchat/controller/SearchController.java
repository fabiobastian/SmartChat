package com.cloudhumans.smartchat.controller;

import com.cloudhumans.smartchat.dto.SearchRequest;
import com.cloudhumans.smartchat.dto.SearchResponse;
import com.cloudhumans.smartchat.service.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("questions")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public SearchResponse getQuestions(@RequestBody SearchRequest searchRequest) {
        return searchService.searchRelevantQuestions(searchRequest);
    }
}

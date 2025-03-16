package com.biztel.ai.controller;

import com.biztel.ai.dto.CrawlResponse;

import com.biztel.ai.dto.CrawlRequest;
import com.biztel.ai.response.ApiResponse;
import com.biztel.ai.service.CrawlService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Web Crawler API", description = "API to crawl web pages")
public class CrawlController {
    @Autowired
    private CrawlService crawlService;

    @PostMapping("/crawl/start")
    @Operation(summary = "Start a new crawl job")
    public ResponseEntity<ApiResponse<CrawlResponse>> startCrawl(@Valid @RequestBody CrawlRequest request) {
        CrawlResponse response = crawlService.startCrawl(request);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", response));
    }

    @GetMapping("/crawl/getById/{id}")
    @Operation(summary = "Get crawl result by ID")
    public ResponseEntity<ApiResponse<CrawlResponse>> getCrawlResult(@PathVariable Long id) {
        CrawlResponse response = crawlService.getCrawlById(id);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", response));
    }

    @GetMapping("/crawl/pagination")
    @Operation(summary = "Get paginated crawl results")
    public ResponseEntity<ApiResponse<Page<CrawlResponse>>> getAllCrawlResults(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CrawlResponse> response = crawlService.getAllCrawlResults(page, size);
        return ResponseEntity.ok(new ApiResponse<>("SUCCESS", response));
    }
}
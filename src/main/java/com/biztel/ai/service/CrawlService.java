package com.biztel.ai.service;

import com.biztel.ai.dto.CrawlResponse;
import com.biztel.ai.dto.CrawlRequest;
import org.springframework.data.domain.Page;

public interface CrawlService {
    CrawlResponse startCrawl(CrawlRequest request);

    CrawlResponse getCrawlById(Long id);

    Page<CrawlResponse> getAllCrawlResults(int page, int pageSize);

}

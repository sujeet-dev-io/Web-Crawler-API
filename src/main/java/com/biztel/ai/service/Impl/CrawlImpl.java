package com.biztel.ai.service.Impl;

import com.biztel.ai.dto.CrawlResponse;
import com.biztel.ai.entity.CrawlResult;
import com.biztel.ai.dto.CrawlRequest;
import com.biztel.ai.enums.CrawlStatus;
import com.biztel.ai.exception.CrawlNotFoundException;
import com.biztel.ai.repository.CrawlRepository;
import com.biztel.ai.service.CrawlService;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class CrawlImpl implements CrawlService {

    @Autowired
    private CrawlRepository crawlRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public CrawlResponse startCrawl(CrawlRequest request) {
        log.info("Starting crawlResult for URL: {}", request.getUrl());

        CrawlResult crawlResult = new CrawlResult();
        crawlResult.setSeedUrl(request.getUrl());
        crawlResult.setStatus(CrawlStatus.IN_PROGRESS);
        crawlResult = crawlRepository.save(crawlResult);

        Long crawlId = crawlResult.getId();

        executorService.submit(() -> performCrawl(crawlId, request.getUrl()));
        CrawlResponse response = modelMapper.map(crawlResult, CrawlResponse.class);
        if (response.getCrawledUrls() == null) {
            response.setCrawledUrls(new ArrayList<>());
        }
        return response;
    }
    private void performCrawl(Long crawlId, String url) {
        try {
            log.info("Fetching links from: {}", url);
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a[href]");
            List<String> crawledUrls = new ArrayList<>();

            for (Element link : links) {
                crawledUrls.add(link.absUrl("href"));
            }
            CrawlResult crawlResult = crawlRepository.findById(crawlId).orElseThrow(() -> new CrawlNotFoundException("CrawlResult job not found"));
            crawlResult.setCrawledUrls(crawledUrls);
            crawlResult.setStatus(CrawlStatus.COMPLETED);
            crawlRepository.save(crawlResult);
            log.info("Crawling completed for ID: {}", crawlId);

        } catch (IOException e) {
            log.error("Crawling failed for ID: {} - {}", crawlId, e.getMessage());
            CrawlResult crawlResult = crawlRepository.findById(crawlId).orElseThrow(() -> new CrawlNotFoundException("CrawlResult job not found"));
            crawlResult.setStatus(CrawlStatus.FAILED);
            crawlRepository.save(crawlResult);
        }
    }

    @Override
    public CrawlResponse getCrawlById(Long id) {
        CrawlResult crawlResult = crawlRepository.findById(id)
                .orElseThrow(() -> new CrawlNotFoundException("CrawlResult job not found with given id..!"));
        return modelMapper.map(crawlResult, CrawlResponse.class);
    }

    @Override
    public Page<CrawlResponse> getAllCrawlResults(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CrawlResult> crawlPage = crawlRepository.findAll(pageable);
        return crawlPage.map(crawlResult -> modelMapper.map(crawlResult, CrawlResponse.class));
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void clearOldCrawlResults() {
        log.info("Clearing old crawl results...");
        crawlRepository.deleteAll();
    }
}

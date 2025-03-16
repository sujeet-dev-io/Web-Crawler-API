package com.biztel.ai.entity;

import com.biztel.ai.enums.CrawlStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class CrawlResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "SeedUrl must not be blank")
    private String seedUrl;


    @ElementCollection
    @CollectionTable(name = "crawled_urls", joinColumns = @JoinColumn(name = "crawl_id"))
    @Column(name = "crawled_urls")
    private List<String> crawledUrls;

    @Enumerated(EnumType.STRING)
    private CrawlStatus status;

    private LocalDateTime createdAt = LocalDateTime.now();
}

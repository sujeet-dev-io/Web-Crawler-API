package com.biztel.ai.dto;

import com.biztel.ai.enums.CrawlStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CrawlResponse {

    private Long crawlId;
    private String seedUrl;
    private List<String> crawledUrls;
    private CrawlStatus status;
    private LocalDateTime createdAt;
}

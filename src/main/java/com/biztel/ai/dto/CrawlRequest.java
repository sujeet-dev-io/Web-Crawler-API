package com.biztel.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrawlRequest {

    @NotBlank(message = "URL cannot be empty")
    private String url;
}
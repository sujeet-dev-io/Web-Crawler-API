package com.biztel.ai.repository;

import com.biztel.ai.entity.CrawlResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CrawlRepository extends JpaRepository<CrawlResult, Long> {
    Page<CrawlResult> findAll(Pageable pageable);
}

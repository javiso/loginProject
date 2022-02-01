package com.example.project.project.service;

import com.example.project.project.model.LogUrl;
import com.example.project.project.repository.LogUrlRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LogUrlService {

    private final LogUrlRepository logUrlRepository;

    public Page<LogUrl> findAll(final Pageable pageable) {
        log.info("Fetching urls visited");
        return logUrlRepository.findAll(pageable);
    }

    public void registerUrl(final String requestURL) {
        log.info("Saving request url {}", requestURL);
        LogUrl logUrl = new LogUrl(requestURL);
        logUrlRepository.save(logUrl);
    }
}
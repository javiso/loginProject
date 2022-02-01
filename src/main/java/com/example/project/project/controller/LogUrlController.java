package com.example.project.project.controller;

import com.example.project.project.mapper.PageApi;
import com.example.project.project.model.LogUrl;
import com.example.project.project.service.LogUrlService;
import lombok.AllArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("project/v1/logUrl")
@AllArgsConstructor
public class LogUrlController {

    private final LogUrlService logUrlService;
    private final MapperFacade mapper;

    @GetMapping
    public ResponseEntity<PageApi<LogUrl>> findAll(@PageableDefault(size = 3) final Pageable pageable){
        return ResponseEntity.ok(mapper.map(logUrlService.findAll(pageable), PageApi.class));
    }
}
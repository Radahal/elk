package com.rgosiewski.psobczyk.pk.elk.logging.controller;

import com.rgosiewski.psobczyk.pk.elk.logging.service.LoggingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logs")
public class LoggingController {
    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping("/generate")
    public void generateLogs() {
        loggingService.generateLogs();
    }
}

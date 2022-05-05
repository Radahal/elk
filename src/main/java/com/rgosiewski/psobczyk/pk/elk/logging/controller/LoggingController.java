package com.rgosiewski.psobczyk.pk.elk.logging.controller;

import com.rgosiewski.psobczyk.pk.elk.logging.service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LoggingController {

    @Autowired
    private LoggingService loggingService;

    @GetMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public void generateLogs() {
        loggingService.generateLogs();
    }
}

package com.rgosiewski.psobczyk.pk.elk;

import com.rgosiewski.psobczyk.pk.elk.logging.enums.Severity;
import com.rgosiewski.psobczyk.pk.elk.logging.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElkApplication {
    private final static Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public static void main(String[] args) {
        SpringApplication.run(ElkApplication.class, args);

        generateLogs();
    }

    public static void generateLogs() {
        for (int order = 0; order < 200; order++) {
            Severity severity = Severity.byOrder(order % 4);
            log(severity, order);
            try {
                Thread.sleep(order % 100);
            } catch (InterruptedException e) {
                logger.error("Sleep interrupted for {}", order, e);
            }
        }
    }

    private static void log(Severity severity, int order) {
        if (Severity.INFO.equals(severity)) {
            info(order);
        } else if (severity.equals(Severity.WARN)) {
            warn(order);
        } else if (severity.equals(Severity.ERROR)) {
            error(order);
        } else if (severity.equals(Severity.DEBUG)) {
            debug(order);
        }
    }

    private static void info(int order) {
        logger.info("Info log {}", order);
    }

    private static void warn(int order) {
        logger.warn("Warn log {}", order);
    }

    private static void error(int order) {
        logger.error("Error log {}", order);
    }

    private static void debug(int order) {
        logger.debug("Debug log {}", order);
    }
}

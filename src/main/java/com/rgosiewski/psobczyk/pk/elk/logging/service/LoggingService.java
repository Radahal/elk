package com.rgosiewski.psobczyk.pk.elk.logging.service;

import com.rgosiewski.psobczyk.pk.elk.logging.enums.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LoggingService {
    private final static Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void generateLogs() {
        Random random = new Random();
        for (int order = 0; order < 1000; order++) {
            long sleepTime = random.nextLong(0, 1000);
            Severity severity = Severity.byOrder(random.nextInt(0, 3));
            log(severity, order);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                logger.error("Sleep interrupted for {}", order, e);
            }
        }
    }

    private void log(Severity severity, int order) {
        if (severity.equals(Severity.INFO)) {
            info(order);
        } else if (severity.equals(Severity.WARN)) {
            warn(order);
        } else if (severity.equals(Severity.ERROR)) {
            error(order);
        } else if (severity.equals(Severity.DEBUG)) {
            debug(order);
        }
    }

    private void info(int order) {
        logger.info("Info log {}", order);
    }

    private void warn(int order) {
        logger.warn("Warn log {}", order);
    }

    private void error(int order) {
        logger.error("Error log {}", order);
    }

    private void debug(int order) {
        logger.debug("Debug log {}", order);
    }
}

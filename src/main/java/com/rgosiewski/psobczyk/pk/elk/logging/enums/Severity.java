package com.rgosiewski.psobczyk.pk.elk.logging.enums;

import java.util.Arrays;

public enum Severity {
    INFO(0),
    WARN(1),
    ERROR(2),
    DEBUG(3);

    private final Integer order;

    Severity(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public static Severity byOrder(Integer order) {
        return Arrays.stream(Severity.values())
                .filter(f -> f.getOrder().equals(order))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}

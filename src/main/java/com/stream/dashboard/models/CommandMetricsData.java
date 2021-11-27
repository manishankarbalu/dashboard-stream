package com.stream.dashboard.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class CommandMetricsData {
    List<Integer> qps;
    HealthStatus commandHealth;
    String commandName;
    CircuitStatus circuitStatus;
    Integer timeoutCount;
    Integer successCount;
    Integer errorCount;
    Integer threadPoolRejectedCount;
    Latency latency;
    Integer total;

    public enum HealthStatus {
        GREEN, YELLOW, RED
    }

    public enum CircuitStatus {
        OPEN, CLOSED
    }

    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Builder
    public static class Latency {
        double p90;
        double p99;
        double p50;
    }
}

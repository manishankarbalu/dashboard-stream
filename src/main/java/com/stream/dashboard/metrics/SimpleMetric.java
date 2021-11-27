package com.stream.dashboard.metrics;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class SimpleMetric {
    String commandName;
    int success;
    int threadPoolRejectedCount;
    List<Double> timeTaken;
    int total;

    public static SimpleMetric reduce(SimpleMetric a, SimpleMetric b) {
        List<Double> timeTaken = new ArrayList<>();
        timeTaken.addAll(a.getTimeTaken());
        timeTaken.addAll(b.getTimeTaken());
        return SimpleMetric.builder()
                .commandName(a.getCommandName())
                .threadPoolRejectedCount(a.getThreadPoolRejectedCount() + b.getThreadPoolRejectedCount())
                .success(a.getSuccess() + b.getSuccess())
                .total(a.getTotal() + b.getTotal())
                .timeTaken(timeTaken)
                .build();
    }
}

package com.stream.dashboard.metrics;

import java.util.*;
import java.util.stream.Collectors;

public class AccumulatorMetricNotifier implements IMetricFetcher {

    private static final AccumulatorMetricNotifier INSTANCE = new AccumulatorMetricNotifier();
    List<SimpleMetric> reservoir = new ArrayList<>();

    public static AccumulatorMetricNotifier getInstance() {
        return INSTANCE;
    }

    public static int getIntFromBool(boolean bool) {
        return bool ? 1 : 0;
    }

    @Override
    public void notify(String commandName, boolean isSuccess, boolean isRejected, double timeTaken) {
        SimpleMetric metric = SimpleMetric.builder().commandName(commandName)
                .success(getIntFromBool(isSuccess))
                .threadPoolRejectedCount(getIntFromBool(isRejected))
                .timeTaken(new ArrayList<>(Collections.singleton(timeTaken)))
                .total(1)
                .build();
        reservoir.add(metric);
    }

    @Override
    public Collection<SimpleMetric> getCuratedSimpleMetrics() {
        Collection<SimpleMetric> collection = reservoir.stream()
                .collect(Collectors.groupingBy(SimpleMetric::getCommandName))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, i -> i.getValue().stream().reduce(SimpleMetric::reduce).get()))
                .values();
        reservoir.clear();
        return collection;
    }
}

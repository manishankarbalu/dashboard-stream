package com.stream.dashboard.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.UniformReservoir;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.dashboard.Utils;
import com.stream.dashboard.models.CommandMetricsData;
import com.stream.dashboard.models.DashboardData;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MetricsAggregator {
    private static final MetricsAggregator INSTANCE = new MetricsAggregator();
    private final Observable<String> singleSource;
    private final IMetricFetcher metricFetcher;
    private final ObjectMapper mapper;

    private MetricsAggregator() {
        mapper = Utils.getMapper();
        metricFetcher = AccumulatorMetricNotifier.getInstance();
        singleSource = Observable.interval(Integer.parseInt(System.getProperty("poll.time")), TimeUnit.MILLISECONDS)
                .map(this::getDashboardData)
                .map(mapper::writeValueAsString)
                .observeOn(Schedulers.io())
                .doOnSubscribe(i -> System.out.println("New subscription"))
                .share();
    }

    public static MetricsAggregator getInstance() {
        return INSTANCE;
    }

    public Observable<String> getStream() {
        return singleSource;
    }


    public DashboardData getDashboardData(Long time) {
        return DashboardData.builder()
                .commandMetricsData(metricFetcher.getCuratedSimpleMetrics()
                        .stream()
                        .map(this::getCommandMetricsFromSample)
                        .collect(Collectors.toList()))
                .build();
    }

    public CommandMetricsData getCommandMetricsFromSample(SimpleMetric simpleMetric) {
        return CommandMetricsData.builder()
                .threadPoolRejectedCount(simpleMetric.getThreadPoolRejectedCount())
                .errorCount(simpleMetric.getTotal() - simpleMetric.getSuccess())
                .total(simpleMetric.getTotal())
                .timeoutCount(1)
                .commandHealth(getHealthStatus(simpleMetric.getTotal(), simpleMetric.getSuccess()))
                .commandName(simpleMetric.getCommandName())
                .circuitStatus(CommandMetricsData.CircuitStatus.CLOSED)
                .qps(new ArrayList<>())//simpleMetric.getTimeTaken().stream().skip(Math.max(0, simpleMetric.getTimeTaken().size() - 10)).mapToInt(Double::intValue).boxed().collect(Collectors.toList()))
                .latency(getLatency(simpleMetric))
                .build();
    }

    public CommandMetricsData.HealthStatus getHealthStatus(int total, int success) {
        float successRatio = success / total;
        if (successRatio > 0.9) {
            return CommandMetricsData.HealthStatus.GREEN;
        } else if (successRatio > 0.7) {
            return CommandMetricsData.HealthStatus.YELLOW;
        } else {
            return CommandMetricsData.HealthStatus.RED;
        }
    }

    public CommandMetricsData.Latency getLatency(SimpleMetric metric) {
        Histogram h = new Histogram(new UniformReservoir(metric.getTimeTaken().size()));
        metric.getTimeTaken().stream().mapToInt(Double::intValue).forEach(h::update);
        return CommandMetricsData.Latency.builder()
                .p50(h.getSnapshot().getMin())
                .p90(h.getSnapshot().get75thPercentile())
                .p99(h.getSnapshot().get99thPercentile())
                .build();
    }
}

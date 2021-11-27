package com.stream.dashboard.metrics;

import java.util.Collection;

public interface IMetricFetcher extends IMetricNotifier {
    Collection<SimpleMetric> getCuratedSimpleMetrics();
}

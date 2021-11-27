package com.stream.dashboard.metrics;

public interface IMetricNotifier {
    void notify(String commandName, boolean isSuccess, boolean isRejected, double timeTaken);
}

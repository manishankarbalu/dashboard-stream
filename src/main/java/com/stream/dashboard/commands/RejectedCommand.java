package com.stream.dashboard.commands;

import com.stream.dashboard.metrics.IMetricNotifier;

import java.util.concurrent.RejectedExecutionException;

public class RejectedCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "RejectedCommand";

    public RejectedCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    @Override
    protected String run() {
        throw new RejectedExecutionException();
    }
}
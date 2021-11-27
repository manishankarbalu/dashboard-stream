package com.stream.dashboard.commands;

import com.stream.dashboard.Utils;
import com.stream.dashboard.metrics.IMetricNotifier;

public class HighLatencyCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "HighLatencyCommand";

    public HighLatencyCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    @Override
    protected String run() {
        Utils.simulateDelay(800, 1200);
        return "Anti Entropy success";
    }
}
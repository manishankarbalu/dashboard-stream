package com.stream.dashboard.commands;

import com.stream.dashboard.Utils;
import com.stream.dashboard.metrics.IMetricNotifier;

public class HighQPSSuccessCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "HighQPSCommand";

    public HighQPSSuccessCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    @Override
    protected String run() {
        Utils.simulateDelay(20, 30);
        return "Success";
    }
}

package com.stream.dashboard.commands;

import com.stream.dashboard.Utils;
import com.stream.dashboard.exception.CommandException;
import com.stream.dashboard.metrics.IMetricNotifier;

public class FailureCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "FailureCommand";

    public FailureCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    @Override
    protected String run() {
        Utils.simulateDelay(30, 50);
        throw new CommandException();
    }
}
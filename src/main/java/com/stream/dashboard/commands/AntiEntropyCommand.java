package com.stream.dashboard.commands;

import com.stream.dashboard.Utils;
import com.stream.dashboard.exception.CommandException;
import com.stream.dashboard.metrics.IMetricNotifier;

public class AntiEntropyCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "AntiEntropyCommand";

    public AntiEntropyCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    private static boolean toggle;

    @Override
    protected String run() {
        Utils.simulateDelay(100, 200);
        toggle = !toggle;
        if (!toggle) {
            throw new CommandException();
        } else {
            return "Anti Entropy success";
        }
    }
}
package com.stream.dashboard.commands;

import com.stream.dashboard.Utils;
import com.stream.dashboard.exception.CommandException;
import com.stream.dashboard.metrics.IMetricNotifier;

import java.util.Random;

public class AntiEntropyCommand extends AbstractMetricsCommand<String> {
    private static final String commandName = "AntiEntropyCommand";
    private final Random random = new Random();

    public AntiEntropyCommand(IMetricNotifier metricNotifier) {
        super(metricNotifier, commandName);
    }

    @Override
    protected String run() {
        Utils.simulateDelay(100, 200);
        if (random.nextInt() < 100) {
            throw new CommandException();
        } else {
            return "Anti Entropy success";
        }
    }
}
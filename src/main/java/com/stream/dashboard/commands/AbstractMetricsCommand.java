package com.stream.dashboard.commands;

import com.stream.dashboard.exception.CommandException;
import com.stream.dashboard.metrics.IMetricNotifier;

import java.util.concurrent.RejectedExecutionException;

public abstract class AbstractMetricsCommand<T> {
    private final IMetricNotifier metricNotifier;
    private final String commandName;

    protected AbstractMetricsCommand(IMetricNotifier metricNotifier, String commandName) {
        this.metricNotifier = metricNotifier;
        this.commandName = commandName;
    }

    protected abstract T run();

    public T execute() {
        boolean isSuccess = true;
        boolean isRejected = false;
        long start = System.currentTimeMillis();
        try {
            return run();
        } catch (RejectedExecutionException exception) {
            isRejected = true;
            isSuccess = false;
            throw exception;
        } catch (CommandException e) {
            isSuccess = false;
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            metricNotifier.notify(commandName, isSuccess, isRejected, end - start);
        }
    }
}

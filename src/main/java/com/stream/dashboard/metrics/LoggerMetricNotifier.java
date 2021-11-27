package com.stream.dashboard.metrics;

public class LoggerMetricNotifier implements IMetricNotifier {

    @Override
    public void notify(String commandName, boolean isSuccess, boolean isRejected, double timeTaken) {
        System.out.println(commandName + isSuccess + isRejected + timeTaken);
    }

}

package com.stream.dashboard;

import com.stream.dashboard.commands.*;
import com.stream.dashboard.metrics.AccumulatorMetricNotifier;
import com.stream.dashboard.servlet.StreamSSEServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class Application {
    public static void main(String[] args) throws Exception {
        System.setProperty("poll.time","1000");
        ServletContextHandler context = new ServletContextHandler(NO_SESSIONS);
        StreamSSEServlet servlet = new StreamSSEServlet();
        context.addServlet(new ServletHolder(servlet), "/dashboard.stream");
        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
        mockCommandInvocation();
    }

    public static void mockCommandInvocation() {
        AccumulatorMetricNotifier metricNotifier = AccumulatorMetricNotifier.getInstance();
        HighQPSSuccessCommand highQPSSuccessCommand = new HighQPSSuccessCommand(metricNotifier);
        FailureCommand failureCommand = new FailureCommand(metricNotifier);
        RejectedCommand rejectedCommand = new RejectedCommand(metricNotifier);
        AntiEntropyCommand antiEntropyCommand = new AntiEntropyCommand(metricNotifier);
        HighLatencyCommand highLatencyCommand = new HighLatencyCommand(metricNotifier);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        try {
            executor.scheduleAtFixedRate(() -> exceptionEater(highQPSSuccessCommand), 100, 3, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(() -> exceptionEater(failureCommand), 100, 100, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(() -> exceptionEater(rejectedCommand), 100, 100, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(() -> exceptionEater(antiEntropyCommand), 100, 50, TimeUnit.MILLISECONDS);
            executor.scheduleAtFixedRate(() -> exceptionEater(highLatencyCommand), 100, 800, TimeUnit.MILLISECONDS);

            //            executor.scheduleAtFixedRate(()->{
//                System.out.println(metricNotifier.getCuratedSimpleMetrics().stream().findFirst().get().getTotal());},1000,1000,TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            System.out.println("exception during mock command invocation");
        }


    }

    public static <T> void exceptionEater(AbstractMetricsCommand<T> supplier) {
        try {
            supplier.execute();
        } catch (Exception e) {
        }
    }
}

package com.stream.dashboard.servlet;

import com.stream.dashboard.metrics.MetricsAggregator;

public class StreamSSEServlet extends SSEServlet {
    //Get the observable and pass
    public StreamSSEServlet() {
        super(MetricsAggregator.getInstance().getStream());
    }
}

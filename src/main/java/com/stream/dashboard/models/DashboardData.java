package com.stream.dashboard.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class DashboardData {
    Collection<CommandMetricsData> commandMetricsData;
}

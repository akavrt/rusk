package com.akavrt.rusk.core;

import com.akavrt.rusk.metrics.MetricProvider;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:07
 */
public interface Solution {
    boolean isFeasible();
    MetricProvider getMetricProvider();
}

package com.akavrt.rusk.metrics;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 14:56
 */
public interface MetricProvider {
    // material usage
    int getUsedMaterialCount();
    int getUsedMaterialAmount();
    // waste
    int getSideTrimAmount();
    double getSideTrimRatio();
    int getTotalTrimAmount();
    double getTotalTrimRatio();
    // patterns
    int getSetupsCount();
    // production
    int getUnderproductionAmount();
    double getAverageUnderproductionRatio();
    double getMaximumUnderproductionRatio();
    int getOverproductionAmount();
    double getAverageOverproductionRatio();
    double getMaximumOverproductionRatio();
}

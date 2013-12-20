package com.akavrt.rusk.ssscsp;

import com.akavrt.rusk.metrics.MetricProvider;

import java.util.Arrays;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 18:49
 */
public class MetricsHelper implements MetricProvider {
    private final Plan plan;
    private final Problem problem;
    private int cachedUsedMaterialCount;
    private int cachedSideTrimAmount;
    private int cachedUnderproductionAmount;
    private double cachedAverageUnderproductionRatio;
    private double cachedMaximumUnderproductionRatio;
    private int cachedOverproductionAmount;
    private double cachedAverageOverproductionRatio;
    private double cachedMaximumOverproductionRatio;
    private int[] productionHolder;

    public MetricsHelper(Plan plan, Problem problem) {
        this.plan = plan;
        this.problem = problem;

        productionHolder = new int[problem.size()];
    }

    @Override
    public int getUsedMaterialCount() {
        if (cachedUsedMaterialCount < 0) {
            int result = 0;
            for (Pattern pattern : plan.getPatterns()) {
                result += pattern.getMultiplier();
            }

            cachedUsedMaterialCount = result;
        }

        return cachedUsedMaterialCount;
    }

    @Override
    public int getUsedMaterialAmount() {
        return getUsedMaterialCount() * problem.getStockWidth();
    }

    @Override
    public int getSideTrimAmount() {
        if (cachedSideTrimAmount < 0) {
            int sideTrim = 0;
            for (Pattern pattern : plan.getPatterns()) {
                int patternWidth = 0;
                for (int i = 0; i < problem.size(); i++) {
                    patternWidth += pattern.getQuantity(i) * problem.getOrder(i).getWidth();
                }

                sideTrim += (problem.getStockWidth() - patternWidth) * pattern.getMultiplier();
            }

            cachedSideTrimAmount = sideTrim;
        }

        return cachedSideTrimAmount;
    }

    @Override
    public double getSideTrimRatio() {
        double used = getUsedMaterialAmount();
        double trim = getSideTrimAmount();
        return used == 0 ? 0 : (trim / used);
    }

    @Override
    public int getTotalTrimAmount() {
        int used = getUsedMaterialAmount();
        int useful = problem.getTotalOrderWidth();

        return used > useful ? (used - useful) : 0;
    }

    @Override
    public double getTotalTrimRatio() {
        double used = getUsedMaterialAmount();
        double useful = problem.getTotalOrderWidth();

        return used > useful ? (1 - useful / used) : 0;
    }

    @Override
    public int getSetupsCount() {
        return plan.getPatterns().size();
    }

    @Override
    public int getUnderproductionAmount() {
        if (cachedUnderproductionAmount < 0) {
            calcProduction();
        }

        return cachedUnderproductionAmount;
    }

    @Override
    public double getAverageUnderproductionRatio() {
        if (cachedAverageUnderproductionRatio < 0) {
            calcProduction();
        }

        return cachedAverageUnderproductionRatio;
    }

    @Override
    public double getMaximumUnderproductionRatio() {
        if (cachedMaximumUnderproductionRatio < 0) {
            calcProduction();
        }

        return cachedMaximumUnderproductionRatio;
    }

    @Override
    public int getOverproductionAmount() {
        if (cachedOverproductionAmount < 0) {
            calcProduction();
        }

        return cachedOverproductionAmount;
    }

    @Override
    public double getAverageOverproductionRatio() {
        if (cachedAverageOverproductionRatio < 0) {
            calcProduction();
        }

        return cachedAverageOverproductionRatio;
    }

    @Override
    public double getMaximumOverproductionRatio() {
        if (cachedMaximumOverproductionRatio < 0) {
            calcProduction();
        }

        return cachedMaximumOverproductionRatio;
    }


    public void commit() {
        cachedUsedMaterialCount = -1;
        cachedSideTrimAmount = -1;
        cachedUnderproductionAmount = -1;
        cachedAverageUnderproductionRatio = -1;
        cachedMaximumUnderproductionRatio = -1;
        cachedOverproductionAmount = -1;
        cachedAverageOverproductionRatio = -1;
        cachedMaximumOverproductionRatio = -1;
    }

    private void calcProduction() {
        Arrays.fill(productionHolder, 0);
        for (Pattern pattern : plan.getPatterns()) {
            for (int i = 0; i < problem.size(); i++) {
                productionHolder[i] += pattern.getProduction(i);
            }
        }

        cachedUnderproductionAmount = 0;
        cachedOverproductionAmount = 0;
        cachedAverageUnderproductionRatio = 0;
        cachedMaximumUnderproductionRatio = 0;
        cachedAverageOverproductionRatio = 0;
        cachedMaximumOverproductionRatio = 0;
        for (int i = 0; i < problem.size(); i++) {
            int produced = productionHolder[i];
            int ordered = problem.getOrder(i).getQuantity();
            if (ordered > produced) {
                // underproduction
                cachedUnderproductionAmount += ordered - produced;

                double underproductionRatio = (ordered - produced) / (double) ordered;
                cachedAverageUnderproductionRatio += underproductionRatio;

                if (underproductionRatio > cachedMaximumUnderproductionRatio) {
                    cachedMaximumUnderproductionRatio = underproductionRatio;
                }
            } else {
                // overproduction
                cachedOverproductionAmount += produced - ordered;

                double overproductionRatio = (produced - ordered) / (double) ordered;
                cachedAverageOverproductionRatio += overproductionRatio;
                if (overproductionRatio > cachedMaximumOverproductionRatio) {
                    cachedMaximumOverproductionRatio = overproductionRatio;
                }
            }
        }

        cachedAverageUnderproductionRatio /= problem.size();
        cachedAverageOverproductionRatio /= problem.size();
    }

}

package com.akavrt.rusk.ssscsp;

import com.akavrt.rusk.core.Solution;
import com.akavrt.rusk.metrics.MetricProvider;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:24
 */
public class Plan implements Solution {
    private final Map<Integer, Pattern> patterns;
    private final Problem problem;
    private boolean useCachedHashCode;
    private int cachedHashCode;
    private MetricsHelper metricsHelper;

    public Plan(Problem problem) {
        this.problem = problem;

        patterns = Maps.newTreeMap();
        metricsHelper = new MetricsHelper(this, problem);

        commit();
    }

    public void addPattern(int[] cuts, int multiplier) {
        if (!Utils.isPatternFeasible(cuts, problem)) {
            return;
        }

        int hash = Pattern.getHash(cuts);
        Pattern pattern = patterns.get(hash);
        if (pattern != null) {
            multiplier += pattern.getMultiplier();

            if (multiplier > 0) {
                pattern.setMultiplier(multiplier);
            } else {
                patterns.remove(hash);
            }
        } else if (multiplier > 0) {
            pattern = new Pattern(cuts, multiplier);
            patterns.put(hash, pattern);
        }

        commit();
    }

    public void removePattern(Pattern pattern) {
        if (patterns.remove(pattern.getQuantitiesHash()) != null) {
            commit();
        }
    }

    @Override
    public boolean isFeasible() {
        return metricsHelper.getUnderproductionAmount() == 0;
    }

    @Override
    public MetricProvider getMetricProvider() {
        return metricsHelper;
    }

    @Override
    public int hashCode() {
        if (!useCachedHashCode) {
            useCachedHashCode = true;

            if (patterns.size() == 0) {
                cachedHashCode = 0;
            } else {
                int[] hashes = new int[patterns.size()];
                int i = 0;
                for (Pattern pattern : patterns.values()) {
                    hashes[i++] = pattern.hashCode();
                }

                cachedHashCode = Arrays.hashCode(hashes);
            }
        }

        return cachedHashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Plan)) {
            return false;
        }

        Plan rhs = (Plan) o;

        return hashCode() == rhs.hashCode();
    }

    Collection<Pattern> getPatterns() {
        return patterns.values();
    }

    private void commit() {
        useCachedHashCode = false;
        metricsHelper.commit();
    }

}

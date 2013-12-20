package com.akavrt.rusk.ssscsp;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: akavrt
 * Date: 20.12.13
 * Time: 02:15
 */
public class MetricsHelperTest {
    private static final double DELTA = 1e-15;
    private Problem problem;

    @Before
    public void setupProblem() {
        List<Order> orders = Lists.newArrayList();
        orders.add(new Order(30, 20));
        orders.add(new Order(40, 50));

        problem = new Problem(orders, 100);
    }

    @Test
    public void usedMaterial() {
        Plan plan = new Plan(problem);
        assertEquals(0, plan.getMetricProvider().getUsedMaterialCount());
        assertEquals(0, plan.getMetricProvider().getUsedMaterialAmount());

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(10, plan.getMetricProvider().getUsedMaterialCount());
        assertEquals(10 * 100, plan.getMetricProvider().getUsedMaterialAmount());

        plan.addPattern(new int[]{0, 2}, 20);
        assertEquals(30, plan.getMetricProvider().getUsedMaterialCount());
        assertEquals(30 * 100, plan.getMetricProvider().getUsedMaterialAmount());
    }

    @Test
    public void trim() {
        Plan plan = new Plan(problem);
        assertEquals(0, plan.getMetricProvider().getSideTrimAmount());
        assertEquals(0, plan.getMetricProvider().getSideTrimRatio(), DELTA);

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(0, plan.getMetricProvider().getSideTrimAmount());
        assertEquals(0, plan.getMetricProvider().getSideTrimRatio(), DELTA);

        plan.addPattern(new int[]{0, 2}, 20);
        assertEquals(20 * 20, plan.getMetricProvider().getSideTrimAmount());
        assertEquals(20 * 20 / 3000d, plan.getMetricProvider().getSideTrimRatio(), DELTA);
        assertEquals(20 * 20, plan.getMetricProvider().getTotalTrimAmount());
        assertEquals(20 * 20 / 3000d, plan.getMetricProvider().getTotalTrimRatio(), DELTA);

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(20 * 20, plan.getMetricProvider().getSideTrimAmount());
        assertEquals(20 * 20 / 4000d, plan.getMetricProvider().getSideTrimRatio(), DELTA);
        assertEquals(20 * 20 + 1000, plan.getMetricProvider().getTotalTrimAmount());
        assertEquals((20 * 20 + 1000) / 4000d, plan.getMetricProvider().getTotalTrimRatio(), DELTA);
    }

    @Test
    public void underproduction() {
        Plan plan = new Plan(problem);
        assertEquals(70, plan.getMetricProvider().getUnderproductionAmount());
        assertEquals(1, plan.getMetricProvider().getMaximumUnderproductionRatio(), DELTA);
        assertEquals(1, plan.getMetricProvider().getAverageUnderproductionRatio(), DELTA);
        assertFalse(plan.isFeasible());

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(40, plan.getMetricProvider().getUnderproductionAmount());
        assertEquals(0.8, plan.getMetricProvider().getMaximumUnderproductionRatio(), DELTA);
        assertEquals(0.4, plan.getMetricProvider().getAverageUnderproductionRatio(), DELTA);
        assertFalse(plan.isFeasible());

        plan.addPattern(new int[]{0, 2}, 20);
        assertEquals(0, plan.getMetricProvider().getUnderproductionAmount());
        assertEquals(0, plan.getMetricProvider().getMaximumUnderproductionRatio(), DELTA);
        assertEquals(0, plan.getMetricProvider().getAverageUnderproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(0, plan.getMetricProvider().getUnderproductionAmount());
        assertEquals(0, plan.getMetricProvider().getMaximumUnderproductionRatio(), DELTA);
        assertEquals(0, plan.getMetricProvider().getAverageUnderproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());
    }

    @Test
    public void overproduction() {
        Plan plan = new Plan(problem);
        assertEquals(0, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(0, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(0, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertFalse(plan.isFeasible());

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(0, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(0, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(0, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertFalse(plan.isFeasible());

        plan.addPattern(new int[]{0, 2}, 20);
        assertEquals(0, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(0, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(0, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());

        plan.addPattern(new int[]{2, 1}, 10);
        assertEquals(30, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(1, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(0.6, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());

        plan.addPattern(new int[]{0, 2}, 20);
        assertEquals(70, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(1, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(1, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());

        plan.addPattern(new int[]{0, 2}, 25);
        assertEquals(120, plan.getMetricProvider().getOverproductionAmount());
        assertEquals(2, plan.getMetricProvider().getMaximumOverproductionRatio(), DELTA);
        assertEquals(1.5, plan.getMetricProvider().getAverageOverproductionRatio(), DELTA);
        assertTrue(plan.isFeasible());
    }

}

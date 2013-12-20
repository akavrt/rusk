package com.akavrt.rusk.ssscsp;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: akavrt
 * Date: 20.12.13
 * Time: 01:35
 */
public class PlanTest {
    private Problem problem;

    @Before
    public void setupProblem() {
        List<Order> orders = Lists.newArrayList(new Order(5, 20));
        problem = new Problem(orders, 10);
    }

    @Test
    public void patternValidity() {
        Plan plan = new Plan(problem);

        plan.addPattern(null, 10);
        plan.addPattern(new int[]{1, 2}, 10);
        plan.addPattern(new int[]{0}, 10);
        plan.addPattern(new int[]{3}, 10);

        assertEquals(0, plan.getMetricProvider().getSetupsCount());

        plan.addPattern(new int[]{2}, 10);
        assertEquals(1, plan.getMetricProvider().getSetupsCount());
    }

    @Test
    public void patternManagement() {
        Plan plan = new Plan(problem);

        plan.addPattern(new int[]{2}, 10);
        assertEquals(1, plan.getMetricProvider().getSetupsCount());
        assertEquals(10, plan.getMetricProvider().getUsedMaterialCount());

        plan.addPattern(new int[]{2}, 10);
        assertEquals(1, plan.getMetricProvider().getSetupsCount());
        assertEquals(20, plan.getMetricProvider().getUsedMaterialCount());

        plan.addPattern(new int[]{1}, 10);
        assertEquals(2, plan.getMetricProvider().getSetupsCount());
        assertEquals(30, plan.getMetricProvider().getUsedMaterialCount());

        plan.addPattern(new int[]{1}, 20);
        assertEquals(2, plan.getMetricProvider().getSetupsCount());
        assertEquals(50, plan.getMetricProvider().getUsedMaterialCount());

        Pattern pattern = new Pattern(new int[]{1}, 1);
        plan.removePattern(pattern);
        assertEquals(1, plan.getMetricProvider().getSetupsCount());
        assertEquals(20, plan.getMetricProvider().getUsedMaterialCount());
    }

    @Test
    public void patternOrdering() {
        Plan first = new Plan(problem);
        first.addPattern(new int[]{2}, 20);
        first.addPattern(new int[]{1}, 10);

        Plan second = new Plan(problem);
        second.addPattern(new int[]{1}, 5);
        second.addPattern(new int[]{2}, 10);
        second.addPattern(new int[]{2}, 5);
        second.addPattern(new int[]{1}, 5);
        second.addPattern(new int[]{2}, 5);

        assertFalse(first == second);
        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());
        assertEquals(2, first.getMetricProvider().getSetupsCount());
        assertEquals(2, second.getMetricProvider().getSetupsCount());

        Plan third = new Plan(problem);
        third.addPattern(new int[]{2}, 20);
        third.addPattern(new int[]{1}, 9);

        assertFalse(first == third);
        assertFalse(first.equals(third));
        assertFalse(first.hashCode() == third.hashCode());
    }

    @Test
    public void planFeasibility() {
        Plan plan = new Plan(problem);

        assertFalse(plan.isFeasible());

        plan.addPattern(new int[]{2}, 9);
        plan.addPattern(new int[]{1}, 1);
        assertFalse(plan.isFeasible());
        assertTrue(plan.getMetricProvider().getUnderproductionAmount() > 0);

        plan.addPattern(new int[]{1}, 1);
        assertTrue(plan.isFeasible());
        assertTrue(plan.getMetricProvider().getUnderproductionAmount() == 0);

        plan.addPattern(new int[]{1}, 1);
        assertTrue(plan.isFeasible());
        assertTrue(plan.getMetricProvider().getUnderproductionAmount() == 0);
    }

}

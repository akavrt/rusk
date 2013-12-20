package com.akavrt.rusk.ssscsp;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: akavrt
 * Date: 20.12.13
 * Time: 01:20
 */
public class ProblemTest {
    private Problem problem;

    @Before
    public void setupProblem() {
        List<Order> orders = Lists.newArrayList();
        orders.add(new Order(10, 20));
        orders.add(new Order(30, 40));
        orders.add(new Order(50, 60));

        problem = new Problem(orders, 100);
    }

    @Test
    public void creation() {
        assertEquals(3, problem.size());
        assertEquals(100, problem.getStockWidth());
    }

    @Test
    public void ordering() {
        Order order = problem.getOrder(1);

        assertEquals(30, order.getWidth());
        assertEquals(40, order.getQuantity());
    }

    @Test
    public void totalAmount() {
        int expected = 10 * 20 + 30 * 40 + 50 * 60;
        assertEquals(expected, problem.getTotalOrderWidth());
    }

}

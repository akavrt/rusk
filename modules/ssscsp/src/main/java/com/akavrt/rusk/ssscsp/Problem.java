package com.akavrt.rusk.ssscsp;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:16
 */
public class Problem {
    private final List<Order> orders;
    private final int stockWidth;
    private final int totalOrderWidth;

    public Problem(List<Order> orders, int stockWidth) {
        this.orders = Lists.newArrayList(orders);
        this.stockWidth = stockWidth;

        totalOrderWidth = calculateTotalOrderWidth();
    }

    public int size() {
        return orders.size();
    }

    public Order getOrder(int index) {
        return orders.get(index);
    }

    public int getStockWidth() {
        return stockWidth;
    }

    public int getTotalOrderWidth() {
        return totalOrderWidth;
    }

    private int calculateTotalOrderWidth() {
        int result = 0;
        for (Order order : orders) {
            result += order.getWidth() * order.getQuantity();
        }

        return result;
    }

}

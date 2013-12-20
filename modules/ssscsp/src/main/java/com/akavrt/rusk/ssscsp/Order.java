package com.akavrt.rusk.ssscsp;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:12
 */
public class Order {
    private final int width;
    private final int quantity;

    public Order(int width, int quantity) {
        this.width = width;
        this.quantity = quantity;
    }

    public int getWidth() {
        return width;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isValid() {
        return width > 0 && quantity > 0;
    }

}

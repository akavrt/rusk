package com.akavrt.rusk.ssscsp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User: akavrt
 * Date: 20.12.13
 * Time: 00:49
 */
public class OrderTest {

    @Test
    public void creation() {
        int width = 100;
        int quantity = 20;
        Order order = new Order(width, quantity);

        assertEquals(width, order.getWidth());
        assertEquals(quantity, order.getQuantity());
    }

    @Test
    public void validity() {
        Order valid = new Order(100, 20);
        assertTrue(valid.isValid());

        Order invalid = new Order(0, 0);
        assertFalse(invalid.isValid());

        invalid = new Order(100, 0);
        assertFalse(invalid.isValid());

        invalid = new Order(0, 20);
        assertFalse(invalid.isValid());
    }

}

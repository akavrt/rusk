package com.akavrt.rusk.ssscsp;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * User: akavrt
 * Date: 20.12.13
 * Time: 00:59
 */
public class PatternTest {

    @Test
    public void production() {
        int[] quantities =  new int[] {1, 2, 3};

        int multiplier = 1;
        Pattern pattern = new Pattern(quantities, multiplier);
        for (int orderIndex = 0; orderIndex < quantities.length; orderIndex++) {
            assertEquals(quantities[orderIndex] * multiplier, pattern.getProduction(orderIndex));
        }

        multiplier = 2;
        pattern = new Pattern(quantities, multiplier);
        for (int orderIndex = 0; orderIndex < quantities.length; orderIndex++) {
            assertEquals(quantities[orderIndex] * multiplier, pattern.getProduction(orderIndex));
        }
    }

    @Test
    public void equality() {
        int[] quantities =  new int[] {1, 2, 3};
        int multiplier = 2;

        Pattern first = new Pattern(quantities, multiplier);
        Pattern second = new Pattern(quantities, multiplier);

        assertFalse(first == second);
        assertTrue(first.equals(second));
        assertTrue(first.hashCode() == second.hashCode());
        assertTrue(first.getQuantitiesHash() == second.getQuantitiesHash());

        first = new Pattern(quantities, 1);
        second = new Pattern(quantities, 2);

        assertFalse(first == second);
        assertFalse(first.equals(second));
        assertFalse(first.hashCode() == second.hashCode());
        assertTrue(first.getQuantitiesHash() == second.getQuantitiesHash());
    }

}


package com.akavrt.rusk.ssscsp;

import com.google.common.base.Objects;

import java.util.Arrays;

/**
 * User: akavrt
 * Date: 19.12.13
 * Time: 16:26
 */
public class Pattern {
    private final int[] quantities;
    private final int quantitiesHash;
    private int multiplier;

    public Pattern(int[] quantities, int multiplier) {
        this.quantities = quantities.clone();
        this.multiplier = multiplier;

        quantitiesHash = getHash(quantities);
    }

    public int getQuantity(int orderIndex) {
        return quantities[orderIndex];
    }

    public int getProduction(int orderIndex) {
        return quantities[orderIndex] * multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getQuantitiesHash() {
        return quantitiesHash;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Arrays.hashCode(quantities), multiplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Pattern)) {
            return false;
        }

        Pattern rhs = (Pattern) o;

        return hashCode() == rhs.hashCode();
    }

    public static int getHash(int[] cuts) {
        return Arrays.hashCode(cuts);
    }

}


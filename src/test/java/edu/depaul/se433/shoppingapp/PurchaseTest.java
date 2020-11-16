package edu.depaul.se433.shoppingapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurchaseTest {
    @Test
    @DisplayName("Makes sure that PurchaseAgent properly handles an empty list of purchases returned by its PurchaseDBO")
    void validToStringOverride() {
        Purchase purchaseShirt = Purchase.make("Blake", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice, "IL", "STANDARD");
        assertTrue(purchaseShirt.toString().contains("date:"));
    }

    @Test
    @DisplayName("Makes sure that PurchaseAgent properly handles an empty list of purchases returned by its PurchaseDBO")
    void validState() {
        Purchase purchaseShirt = Purchase.make("Blake", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice, "IL", "STANDARD");
        assertEquals("IL", purchaseShirt.getState());
    }
}

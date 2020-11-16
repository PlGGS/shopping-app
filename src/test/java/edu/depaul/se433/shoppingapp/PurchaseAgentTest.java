/* 
 * Assignment #4
 * Topic: Mocks
 * Author: <YOUR NAME>
 */
package edu.depaul.se433.shoppingapp;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdbi.v3.core.JdbiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PurchaseAgentTest {
    @Test
    @DisplayName("Makes sure that PurchaseAgent.averagePurchases() actually takes into account every purchase made through its PurchaseDBO")
    void calculatesAveragePurchases() {
        List<Purchase> purchaseList = Arrays.asList(
            Purchase.make("Blake", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice, "IL", "STANDARD"),
            Purchase.make("Kieran", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice * 2, "IL", "STANDARD"),
            Purchase.make("Natalie", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.fancyShirtPrice, "IL", "STANDARD")
        );

        PurchaseDBO mockDBO = mock(PurchaseDBO.class);
        when(mockDBO.getPurchases()).thenReturn(purchaseList);

        PurchaseAgent agent = new PurchaseAgent(mockDBO);
        agent.averagePurchase();

        assertEquals(purchaseList, mockDBO.getPurchases());
    }

    @Test
    @DisplayName("Makes sure that PurchaseAgent properly handles an empty list of purchases returned by its PurchaseDBO")
    void handlesEmptyPurchasesList() {
        List<Purchase> purchaseList = new ArrayList<>();

        PurchaseDBO mockDBO = mock(PurchaseDBO.class);
        when(mockDBO.getPurchases()).thenReturn(purchaseList);

        PurchaseAgent agent = new PurchaseAgent(mockDBO);
        agent.averagePurchase();

        assertTrue(mockDBO.getPurchases().isEmpty());
    }

    //ALL TESTS BELOW SHOULD FAIL BECAUSE THE PROGRAM DOESN'T HANDLE THEM PROPERLY

    @Test
    @DisplayName("Makes sure that PurchaseAgent properly calls its own getPurchases() function to catch JbdiExceptions thrown by its PurchaseDBO's getPurchases() function")
    void handlesJbdiException() {
        PurchaseDBO mockDBO = mock(PurchaseDBO.class);
        when(mockDBO.getPurchases()).thenThrow(JdbiException.class);

        PurchaseAgent agent = new PurchaseAgent(mockDBO);
        agent.averagePurchase();

        assertTrue(mockDBO.getPurchases().isEmpty());
    }
}

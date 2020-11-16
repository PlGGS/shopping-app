package edu.depaul.se433.shoppingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static edu.depaul.se433.shoppingapp.TotalCostCalculatorTest.fancyShirtPrice;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartApiTest {
    ShoppingCartApi cartAPI = new ShoppingCartApi();

    @BeforeEach
    void setup() {
        cartAPI.shoppingCart = cartAPI.shoppingCart();
        cartAPI.purchaseAgent = new PurchaseAgent(mock(PurchaseDBO.class));
    }

    @Test
    @DisplayName("Makes sure that ShoppingCartApi completes checkouts properly")
    void completesCheckout() {
        ShoppingCart apiCart = new ShoppingCart();
        PurchaseItem fancyShirt = new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1);
        apiCart.addItem(fancyShirt);

        assertEquals("ok", cartAPI.checkout("Blake", "IL", "STANDARD"));
    }

    @Test
    @DisplayName("Makes sure that ShoppingCartApi.addItem() confirms that the number of items in its shoppingCart has been updated")
    void confirmsItemAdded() {
        PurchaseItem fancyShirt = new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1);
        assertTrue(cartAPI.addItem(fancyShirt).contains("Cart contains"));
    }

    @Test
    @DisplayName("Makes sure that totals calculated directly through TotalCostCalculator and totals calculated through the ShoppingCartAPI are the same")
    void equalTotals() {
        ShoppingCart directCart = new ShoppingCart();
        ShoppingCart apiCart = new ShoppingCart();

        PurchaseItem fancyShirt = new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1);

        directCart.addItem(fancyShirt);
        apiCart.addItem(fancyShirt);

        cartAPI.shoppingCart = apiCart;

        assertAll(
                () -> assertEquals(TotalCostCalculator.calculate(directCart, "IL", ShippingType.STANDARD), cartAPI.getTotalPrice("IL", "STANDARD")),
                () -> assertEquals(TotalCostCalculator.calculate(directCart, "IL", ShippingType.NEXT_DAY), cartAPI.getTotalPrice("IL", "NEXT_DAY"))
        );
    }

    @Test
    @DisplayName("Makes sure that ShoppingCartApi.getPrice() properly calls its shoppingCart's cost() function")
    void getsPrice() {
        ShoppingCart directCart = new ShoppingCart();
        ShoppingCart apiCart = new ShoppingCart();

        PurchaseItem fancyShirt = new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1);

        directCart.addItem(fancyShirt);
        apiCart.addItem(fancyShirt);

        cartAPI.shoppingCart = apiCart;

        assertEquals(directCart.cost(), cartAPI.getPrice());
    }

    @Test
    @DisplayName("Makes sure that the average number of purchases are the same when calculated through the ShoppingCartAPI")
    void equalAverages() {
        List<Purchase> purchaseList = Arrays.asList(
            Purchase.make("Blake", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice, "IL", "STANDARD"),
            Purchase.make("Kieran", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.shirtPrice * 2, "IL", "STANDARD"),
            Purchase.make("Natalie", LocalDate.of(2000, 4, 4), TotalCostCalculatorTest.fancyShirtPrice, "IL", "STANDARD")
        );

        PurchaseDBO mockDBO = mock(PurchaseDBO.class);
        when(mockDBO.getPurchases()).thenReturn(purchaseList);

        PurchaseAgent directAgent = new PurchaseAgent(mockDBO);
        PurchaseAgent apiAgent = new PurchaseAgent(mockDBO);

        cartAPI.purchaseAgent = apiAgent;

        assertEquals(directAgent.averagePurchase(), cartAPI.getAvergaPurchase());
    }

    //ALL TESTS BELOW SHOULD FAIL BECAUSE THE PROGRAM DOESN'T HANDLE THEM PROPERLY

    @Test
    @DisplayName("Makes sure that ShoppingCartApi.getCart() actually returns its cart")
    void getsCart() {
        assertEquals(cartAPI.shoppingCart, cartAPI.getCart());
    }
}

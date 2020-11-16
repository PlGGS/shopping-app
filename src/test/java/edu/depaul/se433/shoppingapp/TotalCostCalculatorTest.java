package edu.depaul.se433.shoppingapp;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TotalCostCalculatorTest {
  double shirtPrice = 15.99;
  double negativeShirtPrice = -shirtPrice;
  double fancyShirtPrice = 25.99;

//  @BeforeEach
//  void setup() {
//  }

  @Test
  @DisplayName("Makes sure the following totals are valid")
  void validSingleItemCartTotals() {
    double pricePlusStandard = shirtPrice + ShippingType.STANDARD.value;
    double pricePlusNextDay = shirtPrice + ShippingType.NEXT_DAY.value;

    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new PurchaseItem("Shirt", shirtPrice, 1));

    assertAll(
      () -> assertEquals(Precision.round(pricePlusStandard + TaxCalculator.calculate(pricePlusStandard ,"IL"), 2), TotalCostCalculator.calculate(cart, "IL", ShippingType.STANDARD).getTotal()),
      () -> assertEquals(Precision.round(pricePlusNextDay + TaxCalculator.calculate(pricePlusNextDay ,"IL"), 2), TotalCostCalculator.calculate(cart, "IL", ShippingType.NEXT_DAY).getTotal())
    );
  }

  @Test
  @DisplayName("Makes sure the following bills with initial prices instead of carts are valid")
  void validSingleItemInitialCostTotals() {
    assertAll(
      () -> TotalCostCalculator.calculate(10.0, "IL", ShippingType.STANDARD),
      () -> TotalCostCalculator.calculate(15.0, "IL", ShippingType.STANDARD),
      () -> TotalCostCalculator.calculate(20.0, "IL", ShippingType.STANDARD)
    );
  }

  @Test
  @DisplayName("Makes sure the bills for carts with the same amount of items (combined or separate) are valid")
  void equalAmtItemBills() {
    ShoppingCart twoShirtsCart = new ShoppingCart();
    twoShirtsCart.addItem(new PurchaseItem("Shirt", shirtPrice, 2));

    ShoppingCart twoSeparateShirtsCart = new ShoppingCart();
    twoSeparateShirtsCart.addItem(new PurchaseItem("Shirt", shirtPrice, 1));
    twoSeparateShirtsCart.addItem(new PurchaseItem("Shirt", shirtPrice, 1));

    assertAll(
      () -> assertEquals(TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.STANDARD), TotalCostCalculator.calculate(twoSeparateShirtsCart, "IL", ShippingType.STANDARD)),
      () -> assertEquals(TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(twoSeparateShirtsCart, "IL", ShippingType.NEXT_DAY))
    );
  }

  @Test
  @DisplayName("Makes sure bills for carts in different states are equal since all three states have the same tax rate")
  void equalTaxBills() {
    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new PurchaseItem("Shirt", shirtPrice, 1));

    assertAll(
      () -> assertEquals(TotalCostCalculator.calculate(cart, "CA", ShippingType.STANDARD), TotalCostCalculator.calculate(cart, "IL", ShippingType.STANDARD)),
      () -> assertEquals(TotalCostCalculator.calculate(cart, "IL", ShippingType.STANDARD), TotalCostCalculator.calculate(cart, "NY", ShippingType.STANDARD)),
      () -> assertEquals(TotalCostCalculator.calculate(cart, "NY", ShippingType.STANDARD), TotalCostCalculator.calculate(cart, "CA", ShippingType.STANDARD)),
      () -> assertEquals(TotalCostCalculator.calculate(cart, "CA", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(cart, "IL", ShippingType.NEXT_DAY)),
      () -> assertEquals(TotalCostCalculator.calculate(cart, "IL", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(cart, "NY", ShippingType.NEXT_DAY)),
      () -> assertEquals(TotalCostCalculator.calculate(cart, "NY", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(cart, "CA", ShippingType.NEXT_DAY))
    );
  }

  @Test
  @DisplayName("Makes sure the bills for carts with over $50 of merchandise have free shipping")
  void notEqualShipping() {
    ShoppingCart oneShirtCart = new ShoppingCart();
    oneShirtCart.addItem(new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1));

    ShoppingCart twoShirtsCart = new ShoppingCart();
    twoShirtsCart.addItem(new PurchaseItem("Fancy Shirt", fancyShirtPrice, 2));

    assertAll(
      () -> assertNotEquals(TotalCostCalculator.calculate(oneShirtCart, "IL", ShippingType.STANDARD).getShipping(), TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.STANDARD).getShipping()),
      () -> assertNotEquals(TotalCostCalculator.calculate(oneShirtCart, "IL", ShippingType.NEXT_DAY).getShipping(), TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.NEXT_DAY).getShipping())
    );
  }

  //ALL TESTS BELOW SHOULD FAIL BECAUSE THE PROGRAM DOESN'T HANDLE THEM PROPERLY

  @Test
  @DisplayName("Makes sure that negative and zero initial prices throw some sort of exception because of their invalid item prices / quantities")
  void invalidCartTotals() {
    ShoppingCart emptyCart = new ShoppingCart();

    ShoppingCart negativeCart = new ShoppingCart();
    negativeCart.addItem(new PurchaseItem("Negative Shirt", negativeShirtPrice, 1));

    assertAll(
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(emptyCart, "IL", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(negativeCart, "IL", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(emptyCart, "IL", ShippingType.NEXT_DAY)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(negativeCart, "IL", ShippingType.NEXT_DAY))
    );
  }

  @Test
  @DisplayName("Makes sure that negative and zero initial prices throw some sort of exception")
  void invalidInitialCostTotals() {
    assertAll(
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(0.0, "IL", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(-10.0, "IL", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(0.0, "IL", ShippingType.NEXT_DAY)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(-10.0, "IL", ShippingType.NEXT_DAY))
    );
  }

  @Test
  @DisplayName("Makes sure that calculating totals with initial prices vs carts are the same")
  void equalTotals() {
    ShoppingCart oneShirtCart = new ShoppingCart();
    oneShirtCart.addItem(new PurchaseItem("Fancy Shirt", fancyShirtPrice, 1));

    ShoppingCart twoShirtsCart = new ShoppingCart();
    twoShirtsCart.addItem(new PurchaseItem("Fancy Shirt", fancyShirtPrice, 2));

    assertAll(
      () -> assertEquals(TotalCostCalculator.calculate(fancyShirtPrice, "IL", ShippingType.STANDARD), TotalCostCalculator.calculate(oneShirtCart, "IL", ShippingType.STANDARD).getTotal()),
      () -> assertEquals(TotalCostCalculator.calculate(fancyShirtPrice, "IL", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(oneShirtCart, "IL", ShippingType.NEXT_DAY).getTotal()),
      () -> assertEquals(TotalCostCalculator.calculate(fancyShirtPrice * 2, "IL", ShippingType.STANDARD), TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.STANDARD).getTotal()),
      () -> assertEquals(TotalCostCalculator.calculate(fancyShirtPrice * 2, "IL", ShippingType.NEXT_DAY), TotalCostCalculator.calculate(twoShirtsCart, "IL", ShippingType.NEXT_DAY).getTotal())
    );
  }

  @Test
  @DisplayName("Makes sure that invalid states throw some sort of exception")
  void invalidStates() {
    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new PurchaseItem("Shirt", shirtPrice, 1));

    assertAll(
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(cart, "invalid", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(cart, "invalid", ShippingType.NEXT_DAY)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(shirtPrice, "invalid", ShippingType.STANDARD)),
      () -> assertThrows(Exception.class, () -> TotalCostCalculator.calculate(shirtPrice, "invalid", ShippingType.NEXT_DAY))
    );
  }
}

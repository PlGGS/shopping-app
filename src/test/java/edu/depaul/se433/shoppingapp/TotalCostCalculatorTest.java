package edu.depaul.se433.shoppingapp;

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

public class TotalCostCalculatorTest {

  private ShoppingCart cart;
  private TotalCostCalculator calculator;

  @BeforeEach
  void setup() {
    cart = new ShoppingCart();
    calculator = new TotalCostCalculator();
  }

  @Test
  @DisplayName("Makes sure the following bills are valid")
  void validCartTotals() {
    assertAll(
      () -> calculator.calculate(cart, "lol", ShippingType.STANDARD),
      () -> calculator.calculate(cart, "lol", ShippingType.STANDARD),
      () -> calculator.calculate(cart, "lol", ShippingType.STANDARD)
    );
  }

  @Test
  @DisplayName("Makes sure the following bills are valid")
  void validTotals() {
    assertAll(
            () -> calculator.calculate(cart, "lol", ShippingType.STANDARD),
            () -> calculator.calculate(cart, "lol", ShippingType.STANDARD),
            () -> calculator.calculate(cart, "lol", ShippingType.STANDARD)
    );
  }
}

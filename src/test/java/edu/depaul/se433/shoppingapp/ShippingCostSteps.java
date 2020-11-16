package edu.depaul.se433.shoppingapp;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.apache.commons.math3.util.Precision;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShippingCostSteps {
  private static Map<String, String>  stateAbbreviations = new HashMap<>();
  static {
    stateAbbreviations.put("California", "CA");
    stateAbbreviations.put("Illinois", "IL");
    stateAbbreviations.put("New York", "NY");
  }

  private ShoppingCart cart;
  private TotalCostCalculator calculator;
  private Bill bill;

  private int itemQuantity = 0;
  private String itemName = "";
  private double itemPrice = 0.00;
  private String stateAbbreviation = "";
  private ShippingType shippingType = ShippingType.STANDARD;

  private double expectedTotal = 0.00;
  private double expectedShipping = 0.00;
  private double expectedTax = 0.00;

  @Given("The customer bought {int} {string}\\(s) for ${double} each")
  public void the_customer_bought_s_for_$_each(int quantity, String name, double price) {
    this.itemQuantity = quantity;
    this.itemName = name;
    this.itemPrice = Precision.round(price, 2);;
  }

  @And("The customer lives in {string}")
  public void the_driver_was_going_miles_per_hour(String state) {
    this.stateAbbreviation = stateAbbreviations.get(state);
  }

  @And("The customer chose {string} shipping")
  public void theDriverHasId(String type) {
    //Lets the tester enter any form of "STANDARD" or "NEXT_DAY" into Cucumber and get the price of ShippingType.NEXT_DAY
    //Ex. "Standard", "standard", "NEXTDAY", "NextDay", "nextday", "NEXT-DAY", "Next-Day", "next-day", "NEXT DAY", "Next Day", "next day", "Next_Day", "next_day"
    this.shippingType = ShippingType.valueOf(type.toUpperCase().replace(' ', '_').replace('-', '_').replace("TD", "T_D"));
  }

  @Then("The total is ${double} with ${double} shipping and ${double} in tax")
  public void the_fine_is_dollars(double expectedTotal, double expectedShipping, double expectedTax) {
    this.expectedTotal = Precision.round(expectedTotal, 2);
    this.expectedShipping = Precision.round(expectedTotal, 2);
    this.expectedTax = Precision.round(expectedTotal, 2);

    cart = new ShoppingCart();
    cart.addItem(new PurchaseItem(itemName, itemPrice, itemQuantity));

    calculator = new TotalCostCalculator();
    bill = calculator.calculate(cart, stateAbbreviation, shippingType);

    assertAll(
      () -> assertEquals(expectedTotal, bill.getTotal()),
      () -> assertEquals(expectedShipping, bill.getShipping()),
      () -> assertEquals(expectedTax, bill.getTax())
    );
  }
}

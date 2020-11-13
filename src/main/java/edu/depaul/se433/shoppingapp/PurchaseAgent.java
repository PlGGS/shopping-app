/*
 * SE 333 Class project
 * Author: Dan Walker
 * Copyright 2020
 */
package edu.depaul.se433.shoppingapp;

import org.jdbi.v3.core.JdbiException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/**
 * The Purchase agent uses aPurchaseDBO to save and retrieve purchase information.
 * The business logic should be in this class while the details of database connections
 * etc. should be in PurchaseDBO
 */
public class PurchaseAgent {
  private PurchaseDBO dbo;

  public PurchaseAgent(PurchaseDBO dbo) {
    this.dbo = dbo;
  }

  public void save(Purchase purchase) {
    dbo.savePurchase(purchase);
  }

  public List<Purchase> getPurchases() {
    try {
      return dbo.getPurchases();
    } catch (JdbiException e) {
      return new ArrayList<>();
    }
  }

  public double averagePurchase() {
    List<Purchase> purchases = dbo.getPurchases();
    OptionalDouble avg = purchases.stream()
        .mapToDouble(purchase -> purchase.getCost())
        .average();
    if (avg.isPresent()) {
      return avg.getAsDouble();
    } else {
      return 0.0;
    }
  }
}

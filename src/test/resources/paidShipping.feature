Feature: Paid Shipping Total Cost Calculation
  Total Cost Calculation for bills UNDER $50

  Scenario: Customer ordered 1 $15.99 shirt with standard shipping
    Given The customer bought 1 "Shirt"(s) for $15.99 each
    And The customer lives in "Illinois"
    And The customer chose "Standard" shipping
    Then The total is $27.55 with $10.00 shipping and $1.56 in tax

  Scenario: Customer ordered 1 $15.99 shirt with next-day shipping
    Given The customer bought 1 "Shirt"(s) for $15.99 each
    And The customer lives in "Illinois"
    And The customer chose "Next-Day" shipping
    Then The total is $43.45 with $25.00 shipping and $2.46 in tax

Feature: Free Shipping Total Cost Calculation
  Total Cost Calculation for bills OVER $50

  Scenario: Customer ordered 2 $25.99 fancy shirts with standard shipping
    Given The customer bought 2 "Fancy Shirt"(s) for $25.99 each
    And The customer lives in "Illinois"
    And The customer chose "Standard" shipping
    Then The total is $55.10 with $0.00 shipping and $3.12 in tax

  Scenario: Customer ordered 2 $25.99 fancy shirts with next-day shipping
    Given The customer bought 2 "Fancy Shirt"(s) for $25.99 each
    And The customer lives in "Illinois"
    And The customer chose "Next-Day" shipping
    Then The total is $55.10 with $0.00 shipping and $3.12 in tax

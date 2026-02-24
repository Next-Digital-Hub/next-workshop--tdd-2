Feature: Manage Budget
  In order to maintain the order system
  As an API user
  I want to add the initial budget.
  I want to create, update and delete expenses.
  I want to know my remaining budget.

  Scenario: Add initial Budget
    Given an initial quantity of 100
    When the initial budget is saved
    Then the initial budget is persisted successfully

  Scenario: Create an affordable expense
    Given a budget of 100 and an expense with concept "Book" and cost 12
    When the expense is saved
    Then the expense is persisted succesfully

  Scenario: Create an unaffordable expense
    Given a budget of 100 and an expense with concept "Trip to Indonesia" and cost 1200
    When the expense is saved
    Then you get the message "Expense out of budget"

  Scenario: Update an expense concept or cost
    Given a budget of 100, an expense with concept "Book" and cost 12 and an expense id 0, and a new expense concept "Chess" and cost 15
    When the expense is updated
    Then the expense concept is persisted as concept "Chess" and cost 15

  Scenario: Delete an expense
    Given a budget of 100, an expense with concept "Book" and cost 12 and an expense id 0
    When the expense is deleted
    Then the expense no longer exists and the budget is updated

  Scenario: Get remaining budget
    Given a budget of 100 an an expense with concept "Book" and cost 15
    When I request the final budget
    Then I should get the number of remaining budget, should be 85

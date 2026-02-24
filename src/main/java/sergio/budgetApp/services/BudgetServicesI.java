package sergio.budgetApp.services;


import sergio.budgetApp.domain.Budget;
import sergio.budgetApp.domain.Expense;

import java.util.UUID;

public interface BudgetServicesI {

    public UUID createBudget(Budget budget);
    public Budget getBudget(UUID id);
    public void addExpense(UUID bugetId, Expense newExpense);
    public void editExpense(UUID bugetId, int index, Expense newExpense);
    public void removeExpense(UUID bugetId, int index);
}

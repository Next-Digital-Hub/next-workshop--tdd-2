package sergio.budgetApp.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sergio.budgetApp.domain.Budget;
import sergio.budgetApp.domain.Expense;
import sergio.budgetApp.repositories.BudgetRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetServices implements BudgetServicesI {
    private final BudgetRepository budgetRepository;

    public UUID createBudget(Budget budget){
        Budget newBudget = budgetRepository.save(budget);
        return newBudget.getId();
    }

    public Budget getBudget(UUID id){
        return budgetRepository.findById(id).orElse(null);
    }

    public void addExpense(UUID bugetId, Expense newExpense){
        Budget budget = budgetRepository.findById(bugetId).orElse(null);
        if(budget != null){
            budget.getExpenses().add(newExpense);
            budgetRepository.save(budget);
        }
    }

    public void editExpense(UUID bugetId, int index, Expense newExpense){
        Budget budget = budgetRepository.findById(bugetId).orElse(null);
        if (budget != null) {
            List<Expense> expenses = budget.getExpenses();
            if (index >= 0 && index < expenses.size()) {
                Expense expenseToEdit = expenses.get(index);
                expenseToEdit.setConcept(newExpense.getConcept());
                expenseToEdit.setAmount(newExpense.getAmount());
                budgetRepository.save(budget);
            } else {
                System.out.println("Error: El íd " + index + " no existe para el presupuesto.");
            }
        }
    }

    public void removeExpense(UUID bugetId, int index){
        Budget budget = budgetRepository.findById(bugetId).orElse(null);
        if(budget!=null) {
            List<Expense> expenses = budget.getExpenses();
            if (index >= 0 && index < expenses.size()) {
                expenses.remove(index);
                budget.setExpenses(expenses);
                budgetRepository.save(budget);
            } else {
                System.out.println("Error: El íd " + index + " no existe para el presupuesto.");
            }
        }
    }
}

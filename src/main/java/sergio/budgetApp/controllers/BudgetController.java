package sergio.budgetApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sergio.budgetApp.domain.Budget;
import sergio.budgetApp.domain.Expense;
import sergio.budgetApp.services.BudgetServicesI;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetServicesI budgetServicesI;
    private UUID myBudget;

    @PostMapping("/createBudget/{quantity}")
    public ResponseEntity<Budget> createBudget(@PathVariable int quantity){
        Budget newBudget = Budget.builder().initialBudget(quantity).build();
        myBudget = budgetServicesI.createBudget(newBudget);
        return new ResponseEntity<>(budgetServicesI.getBudget(myBudget), HttpStatus.CREATED);
    }

    @GetMapping("/getMyRemainingBudget")
    public ResponseEntity<Integer> getMyRemainingBudget(){
        Budget budget = budgetServicesI.getBudget(myBudget);
        if(budget!=null){
            return new ResponseEntity<>(budget.calculateRemainingBudget(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getMyBudget")
    public ResponseEntity<Budget> getMyBudget(){
        Budget budget = budgetServicesI.getBudget(myBudget);
        if(budget!=null){
            return new ResponseEntity<>(budget, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addExpense")
    public ResponseEntity<String> addExpense(@RequestBody AddExpenseRequest addExpenseRequest){
        if(budgetServicesI.getBudget(myBudget)!= null && addExpenseRequest.amount() <= budgetServicesI.getBudget(myBudget).calculateRemainingBudget()) {
            Expense newExpense = Expense.builder().concept(addExpenseRequest.concept()).amount(addExpenseRequest.amount()).build();
            budgetServicesI.addExpense(myBudget, newExpense);
            return ResponseEntity.ok("Ok");
        }else{
            return ResponseEntity.badRequest().body("Expense out of budget");
        }
    }

    @PostMapping("/editExpense")
    public ResponseEntity<String> editExpense(@RequestBody EditExpenseRequest editExpenseRequest){
        Expense newExpense = Expense.builder().concept(editExpenseRequest.newConcept()).amount(editExpenseRequest.newAmount()).build();
        budgetServicesI.editExpense(myBudget, editExpenseRequest.id(), newExpense);
        Expense editedExpense = budgetServicesI.getBudget(myBudget).getExpenses().get(editExpenseRequest.id());
        if(
            editedExpense.getConcept().equals(editExpenseRequest.newConcept()) &&
            editedExpense.getAmount()==editExpenseRequest.newAmount()
        ){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/removeExpense/{id}")
    public ResponseEntity<String> removeExpense(@PathVariable int id){
        int expenses = budgetServicesI.getBudget(myBudget).getExpenses().size();
        budgetServicesI.removeExpense(myBudget, id);
        if(budgetServicesI.getBudget(myBudget).getExpenses().size() == expenses - 1){
            return ResponseEntity.ok("Expense Removed");
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

}


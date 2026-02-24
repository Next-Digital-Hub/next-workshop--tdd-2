package sergio.budgetApp.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sergio.budgetApp.domain.Budget;
import sergio.budgetApp.controllers.*;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetSteps {

    @Autowired
    private TestRestTemplate restTemplate;
    private Budget budget;
    private int expenseId;
    AddExpenseRequest addRequest;
    EditExpenseRequest editRequest;
    private ResponseEntity<?> response;

    // ---------- GIVEN ----------

    @Given("an initial quantity of {int}")
    public void givenBudget(int quantity) {
        budget = Budget.builder().initialBudget(quantity).build();
    }

    @Given("a budget of {int} and an expense with concept {string} and cost {int}")
    public void givenExpense(int quantity, String concept, int amount) {
        restTemplate.postForEntity("/createBudget/" + quantity, null, Budget.class);
        addRequest = new AddExpenseRequest(concept, amount);
    }

    @Given("a budget of {int}, an expense with concept {string} and cost {int} and an expense id {int}, and a new expense concept {string} and cost {int}")
    public void givenExpenseIdAndNewExpense(int quantity, String concept, int amount, int id, String newConcept, int newAmount) {
        restTemplate.postForEntity("/createBudget/" + quantity, null, Budget.class);
        addRequest = new AddExpenseRequest(concept, amount);
        editRequest = new EditExpenseRequest(id, newConcept, newAmount);
    }

    @Given("a budget of {int}, an expense with concept {string} and cost {int} and an expense id {int}")
    public void givenExpenseId(int quantity, String concept, int amount, int id) {
        restTemplate.postForEntity("/createBudget/" + quantity, null, Budget.class);
        addRequest = new AddExpenseRequest(concept, amount);
        expenseId = id;
    }

    @Given("a budget of {int} an an expense with concept {string} and cost {int}")
    public void givenBudget(int quantity, String concept, int amount) {
        restTemplate.postForEntity("/createBudget/" + quantity, null, Budget.class);
        addRequest = new AddExpenseRequest(concept, amount);
    }

    // ---------- WHEN ----------

    @When("the initial budget is saved")
    public void whenBudgetIsSaved() {
        int quantity = budget.getInitialBudget();
        response = restTemplate.postForEntity("/createBudget/" + quantity, null, void.class);
    }

    @When("the expense is saved")
    public void whenExpenseIsSaved() {
        response = restTemplate.postForEntity("/addExpense", addRequest, String.class);
    }

    @When("the expense is updated")
    public void whenExpenseIsUpdated() {
        restTemplate.postForEntity("/addExpense", addRequest, String.class);
        response = restTemplate.postForEntity("/editExpense", editRequest, void.class);
    }

    @When("the expense is deleted")
    public void whenExpenseIsDeleted() {
        restTemplate.postForEntity("/addExpense", addRequest, String.class);
        response = restTemplate.exchange(
                "/removeExpense/" + expenseId,
                HttpMethod.DELETE,
                null,
                String.class
        );
    }

    @When("I request the final budget")
    public void whenGetMyFinalBudget() {
        restTemplate.postForEntity("/addExpense", addRequest, String.class);
        response = restTemplate.getForEntity("/getMyRemainingBudget", Integer.class);
    }

    // ---------- THEN ----------

    @Then("the initial budget is persisted successfully")
    public void thenBudgetPersistedSuccessfully() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("the expense is persisted succesfully")
    public void thenExpensePersistedSuccessfully() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Ok");
    }

    @Then("you get the message {string}")
    public void thenGetMessage(String message) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(message);
    }

    @Then("the expense concept is persisted as concept {string} and cost {int}")
    public void thenConceptAndCostPersisted(String concept, int cost) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        budget = restTemplate.getForEntity("/getMyBudget", Budget.class).getBody();
        assertThat(budget.getExpenses().get(expenseId).getConcept()).isEqualTo(concept);
        assertThat(budget.getExpenses().get(expenseId).getAmount()).isEqualTo(cost);
    }

    @Then("the expense no longer exists and the budget is updated")
    public void thenExpenseNotExist() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("I should get the number of remaining budget, should be {int}")
    public void thenResponseStatusShouldBe(Integer remainingBudget) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(remainingBudget);
    }
}

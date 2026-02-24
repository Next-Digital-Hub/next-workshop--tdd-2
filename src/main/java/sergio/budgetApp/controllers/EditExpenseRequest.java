package sergio.budgetApp.controllers;

public record EditExpenseRequest(int id, String newConcept, int newAmount) {}

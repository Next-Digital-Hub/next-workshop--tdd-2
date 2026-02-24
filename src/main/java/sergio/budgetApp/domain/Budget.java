package sergio.budgetApp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false)
    private int initialBudget;

    @ElementCollection
    private List<Expense> expenses;

    public int calculateRemainingBudget(){
        int finalBudget = initialBudget;
        for (Expense expense : expenses){
            finalBudget -= expense.getAmount();
        }
        return finalBudget;
    }
}

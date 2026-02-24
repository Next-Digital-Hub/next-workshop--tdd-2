package sergio.budgetApp.domain;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    private String concept;
    private int amount;
}
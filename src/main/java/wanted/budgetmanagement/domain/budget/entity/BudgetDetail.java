package wanted.budgetmanagement.domain.budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.budgetmanagement.domain.Category;

import java.time.Month;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDetail {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    private Integer amount; // 예산

    private Integer percent;

    private Month month;
}

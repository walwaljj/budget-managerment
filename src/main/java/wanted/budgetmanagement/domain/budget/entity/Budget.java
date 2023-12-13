package wanted.budgetmanagement.domain.budget.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Month;
import java.util.List;


/**
 * 예산. 지출 계획
 */
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private Integer budget; // 총 예산

    @OneToMany
    private List<BudgetDetail> budgetDetails;

    private Month month; // 예산이 적용될 월



}

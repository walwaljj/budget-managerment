package wanted.budgetmanagement.domain.budget.dto;

import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.budget.entity.BudgetDetail;

import java.time.Month;
import java.util.List;

@Builder
@Getter
public class BudgetRequestDto {

    private Integer budget; // 총 예산

    @OneToMany(mappedBy = "budget")
    private List<BudgetDetail> budgetDetail;

    private Month month; // 예산이 적용될 월

}

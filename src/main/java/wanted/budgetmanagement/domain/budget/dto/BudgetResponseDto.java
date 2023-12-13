package wanted.budgetmanagement.domain.budget.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.budget.entity.Budget;
import wanted.budgetmanagement.domain.budget.entity.BudgetDetail;

import java.util.List;

@Builder
@Getter
public class BudgetResponseDto {


    private Integer budget; // 총 예산

    private List<BudgetDetail> budgetDetails;

    public static BudgetResponseDto toBudgetResponseDto(Budget budget){
        return BudgetResponseDto.builder()
                .budget(budget.getBudget())
                .budgetDetails(budget.getBudgetDetails())
                .build();
    }
}

package wanted.budgetmanagement.domain.budget.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Month;

@Builder
@Getter
public class BudgetRecommendationRequestDto {

    private Integer totalBudget; // 총 예산

    private Month month;

}

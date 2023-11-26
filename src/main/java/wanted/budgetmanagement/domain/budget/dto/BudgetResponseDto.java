package wanted.budgetmanagement.domain.budget.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.budget.entity.Budget;

import java.time.Month;

@Builder
@Getter
public class BudgetResponseDto {

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    private Integer budget; // 예산

    private Month month;

    public static BudgetResponseDto toBudgetResponseDto(Budget budget){
        return BudgetResponseDto.builder()
                .category(budget.getCategory())
                .budget(budget.getBudget())
                .month(budget.getMonth())
                .build();
    }
}

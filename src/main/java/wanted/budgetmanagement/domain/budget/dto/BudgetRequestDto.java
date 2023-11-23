package wanted.budgetmanagement.domain.budget.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.Category;

@Builder
@Getter
public class BudgetRequestDto {

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    private Integer budget; // 예산

}

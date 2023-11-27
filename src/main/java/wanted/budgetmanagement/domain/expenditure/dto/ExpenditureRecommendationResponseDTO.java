package wanted.budgetmanagement.domain.expenditure.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
public class ExpenditureRecommendationResponseDTO {

    private Integer todayBudget;

    private LocalDate date;

    private String message;
}

package wanted.budgetmanagement.domain.expenditure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.Category;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class TotalAmountResponseDto {

    private Category category;

    private Integer integer;

}

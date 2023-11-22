package wanted.budgetmanagement.domain.expenditure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import wanted.budgetmanagement.domain.Category;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalAmountResponseDto {

    private Category category;

    private Integer amount;
}

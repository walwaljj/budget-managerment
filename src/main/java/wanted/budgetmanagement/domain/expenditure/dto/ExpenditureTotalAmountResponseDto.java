package wanted.budgetmanagement.domain.expenditure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.util.List;

/**
 * ExpenditureTotalAmountResponseDto
 * 목록과 함께 지출 합계를 보여주는 Dto
 */
@Getter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpenditureTotalAmountResponseDto {

    private Integer userId;

    private List<Expenditure> expenditureList; // 지출 목록

    private Integer totalAmountByDate; // 기간별 지출 합계

    private List<TotalAmountResponseDto> totalAmountByCategory; // 카테고리 지출 합계

}

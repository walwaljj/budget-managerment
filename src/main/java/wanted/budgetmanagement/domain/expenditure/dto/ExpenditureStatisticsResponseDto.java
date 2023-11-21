package wanted.budgetmanagement.domain.expenditure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * 지출 통계를 보여줄 Dto
 */
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ExpenditureStatisticsResponseDto {

    @JsonIgnore
    private Integer userId;

    private LocalDate today; // 오늘

    private DayOfWeek day; // 요일 정보

    private LocalDate startDate; // 조회 시작 기준 일

    private LocalDate endDate; // 조회 기준 일

    @Setter
    private Integer thisMonthTotalAmount; // 기준일 소비 총액

    @Setter
    private Integer searchedResultTotalAmount; // 조회 된 소비 총액

    @Setter
    private Integer rate; // 소비율

    private List<TotalAmountResponseDto> totalAmountByCategory;
}

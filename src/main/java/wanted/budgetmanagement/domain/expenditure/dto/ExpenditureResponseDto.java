package wanted.budgetmanagement.domain.expenditure.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.time.LocalDate;

/**
 * ExpenditureResponseDto
 */
@Getter
@Builder(toBuilder = true)
public class ExpenditureResponseDto {

    private Integer userId;

    @Enumerated(EnumType.STRING)
    private Category category; // 지출 카테고리

    private Integer amount; // 지출 금액

    private LocalDate date; // 지출일

    private String memo; // 지출 메모

    // entity 를 dto 로 변환하는 정적 메서드
    public static ExpenditureResponseDto toResponseDto(Expenditure expenditure){
        return ExpenditureResponseDto.builder()
                .userId(expenditure.getUserId())
                .category(expenditure.getCategory())
                .amount(expenditure.getAmount())
                .date(expenditure.getDate())
                .memo(expenditure.getMemo())
                .build();
    }

}

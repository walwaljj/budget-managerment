package wanted.budgetmanagement.domain.expenditure.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.Category;
import wanted.budgetmanagement.domain.expenditure.entity.Expenditure;

import java.time.LocalDate;

/**
 * ExpenditureRequestDto
 */
@Getter
@Builder(toBuilder = true)
public class ExpenditureRequestDto {

    @JsonIgnore
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "지출 카테고리", example = "음식")
    private Category category; // 지출 카테고리

    @Schema(description = "지출 금액", example = "5000")
    private Integer amount; // 지출 금액

    @Schema(description = "지출일", example = "2023-11-19")
    private LocalDate date; // 지출일

    @Schema(description = "지출 메모", example = "스타벅스 바질 포카치아")
    private String memo; // 지출 메모

    // 입력받은 정보를 ExpenditureRequestDto 로 변환하는 메서드
    public static ExpenditureRequestDto createRequestDto(
                                                       String category,
                                                       Integer amount,
                                                       String memo){
        return ExpenditureRequestDto.builder()
                .category(Category.valueOf(category))
                .amount(amount)
                .date(LocalDate.now())
                .memo(memo)
                .build();
    }

    // 요청을 entity 로 변환하는 메서드
    public static Expenditure fromRequestDto(Integer userId, ExpenditureRequestDto requestDto){
        return Expenditure.builder()
                .userId(userId)
                .category(requestDto.getCategory())
                .amount(requestDto.getAmount())
                .date(requestDto.getDate())
                .memo(requestDto.getMemo())
                .build();
    }

}

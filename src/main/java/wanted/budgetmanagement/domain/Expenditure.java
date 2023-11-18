package wanted.budgetmanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 지출.
 */
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Expenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 예시로 Long 형식의 ID를 사용

    private Integer userId;

    @Enumerated(EnumType.STRING)
    private Category category; // 지출 카테고리

    private Integer amount; // 지출 금액

    private LocalDate date; // 지출일

    private String memo; // 지출 메모



}

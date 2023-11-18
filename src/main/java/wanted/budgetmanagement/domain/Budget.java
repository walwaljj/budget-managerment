package wanted.budgetmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예산. 지출 계획
 */
@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Id
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    private Integer budget; // 예산
}

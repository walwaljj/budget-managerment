package wanted.budgetmanagement.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId; // 유저의 id

    private String webHookUrl; // 웹훅 주소

    private boolean alarmEnabled; // 알람 활성화 여부 - 기본 true
}

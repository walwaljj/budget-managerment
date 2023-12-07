package wanted.budgetmanagement.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.user.entity.Alert;

@Builder
@Getter
public class AlertResponseDto {

    private Integer userId; // 유저의 id

    private String webHookUrl; // 웹훅 주소

    private boolean alarmEnabled; // 알람 활성화 여부 - 기본 true

    public static AlertResponseDto of(Alert alert){
        return AlertResponseDto.builder()
                .userId(alert.getUserId())
                .webHookUrl(alert.getWebHookUrl())
                .alarmEnabled(alert.isAlarmEnabled())
                .build();
    }
}

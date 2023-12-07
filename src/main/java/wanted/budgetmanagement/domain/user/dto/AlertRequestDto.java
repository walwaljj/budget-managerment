package wanted.budgetmanagement.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import wanted.budgetmanagement.domain.user.entity.Alert;

@Builder
@Getter
public class AlertRequestDto {

    private String webHookUrl; // 웹훅 주소

    private boolean alarmEnabled; // 알람 활성화 여부 - 기본 true

    public static AlertRequestDto of(Alert alert){
        return AlertRequestDto.builder()
                .webHookUrl(alert.getWebHookUrl())
                .alarmEnabled(alert.isAlarmEnabled())
                .build();
    }
}

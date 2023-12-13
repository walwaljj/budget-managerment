package wanted.budgetmanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // USER
    DUPLICATED_USER(HttpStatus.CONFLICT, "이미 사용중인 ID 입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, " 사용자를 찾을 수 없습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),
    USER_ALERT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 알람 설정 정보를 찾을 수 없습니다."),

    // EXPENDITURE
    EXPENDITURE_NOT_FOUND(HttpStatus.NOT_FOUND, " 지출 내역을 찾을 수 없습니다."),
    EXPENDITURE_DELETION_FAILED(HttpStatus.NOT_FOUND, " 지출 내역 삭제 실패."),

    // SEARCH
    INVALID_RANGE(HttpStatus.BAD_REQUEST, "금액 범위를 확인해주세요."),

    // BUDGET,
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "예산을 찾을 수 없습니다."),
    INVALID_AMOUNT_OR_PERCENT(HttpStatus.BAD_REQUEST, "예산 설정 금액 또는 퍼센트를 확인해주세요. 카테고리 금액 합이 설정하는 총 예산과 같게 설정하거나, 퍼센트의 합을 100 으로 맞춰 주세요."),
    BUDGET_EXISTS(HttpStatus.CONFLICT,"이미 설정한 예산이 있습니다." );


    private HttpStatus status;

    String msg;

    ErrorCode(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}

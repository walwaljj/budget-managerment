package wanted.budgetmanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // USER
    DUPLICATED_USER(HttpStatus.CONFLICT, "이미 사용중인 ID 입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, " 사용자를 찾을 수 없습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다.");

    private HttpStatus status;

    String msg;

    ErrorCode( HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}

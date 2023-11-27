package wanted.budgetmanagement.response;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public enum ResponseCode {

    // EXPENDITURE
    EXPENDITURE_CREATE(HttpStatus.CREATED, "201", "지출 등록 성공"),
    EXPENDITURE_UPDATE(HttpStatus.CREATED, "201", "지출 수정 성공"),
    EXPENDITURE_DELETE(HttpStatus.NO_CONTENT, "201", "지출 삭제 성공"),
    EXPENDITURE_READ(HttpStatus.OK, "200", "지출 상세 조회 성공"),
    EXPENDITURE_LIST_READ(HttpStatus.OK, "200", "지출 전체 조회 성공"),
    EXPENDITURE_RATE(HttpStatus.OK, "200", "소비율 조회 성공"),
    EXPENDITURE_SEARCH(HttpStatus.OK, "200", "지출 검색 성공"),
    EXPENDITURE_RECOMMEND(HttpStatus.OK, "200", "금일 지출 추천 성공"),

    // USER
    USER_CREATE(HttpStatus.CREATED, "201", "회원 가입 성공"),
    USER_LOGIN(HttpStatus.OK, "200", "로그인 성공"),
    USER_LOGOUT(HttpStatus.NO_CONTENT, "204", "로그 아웃 성공"),

    // BUDGET,
    BUDGET_SETTING(HttpStatus.CREATED, "201", "예산 설정 성공"),
    BUDGET_RECOMMENDATION(HttpStatus.CREATED, "201", "예산 추천 성공");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public ResponseEntity<CommonResponse> toResponse(Object data) {
        return new ResponseEntity<>(CommonResponse.builder()
                .responseCode(this)
                .code(this.code)
                .message(this.message)
                .data(data)
                .build(), httpStatus.OK);
    }
}

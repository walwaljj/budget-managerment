package wanted.budgetmanagement.response;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public enum ResponseCode {

    // ARTICLE
    ARTICLE_CREATE(HttpStatus.CREATED, "201", "게시글 등록 성공"),
    ARTICLE_UPDATE(HttpStatus.CREATED, "201", "게시글 수정 성공"),
    ARTICLE_DELETE(HttpStatus.NO_CONTENT, "201", "게시글 삭제 성공"),
    ARTICLE_READ(HttpStatus.OK,"200", "게시글 조회 성공"),
    ARTICLE_LIST_READ(HttpStatus.OK,"200", "게시글 전체 조회 성공"),
    ARTICLE_LIKES_READ(HttpStatus.OK,"200", "게시글 좋아요 조회 성공"),
    COMMENT_CREATE(HttpStatus.OK,"200", "댓글 등록 완료"),
    COMMENT_DELETE(HttpStatus.NO_CONTENT,"201", "댓글 삭제 완료"),

    // USER
    USER_CREATE(HttpStatus.CREATED, "201", "회원 가입 성공"),
    USER_LOGIN(HttpStatus.OK, "200", "로그인 성공"),
    USER_LOGOUT(HttpStatus.NO_CONTENT, "204", "로그 아웃 성공"),
    USER_PROFILE_UPDATE(HttpStatus.OK, "200", "사용자 프로필 변경 완료"),
    USER_PROFILE_READ(HttpStatus.OK, "200", "사용자 프로필 조회 성공"),
    USER_LIKES_ARTICLE(HttpStatus.OK, "200", "사용자 게시글 좋아요 성공"),
    USER_LIKES_CANCEL_ARTICLE(HttpStatus.NO_CONTENT, "204", "사용자 게시글 좋아요 취소 성공"),
    USER_LIKES_READ(HttpStatus.OK, "200", "사용자 좋아요 목록 조회 성공"),

    // FOLLOW
    USER_FOLLOW_REQUEST(HttpStatus.OK,"201","팔로우 신청 성공"),
    USER_FOLLOW_CANCEL(HttpStatus.NO_CONTENT,"201","팔로우 취소 완료"),
    USER_FOLLOW_ARTICLE_LIST(HttpStatus.OK,"200","팔로우한 사용자의 게시글 조회 완료"),

    // FRIEND
    USER_FRIEND_REQUEST(HttpStatus.OK,"200","친구 신청 완료"),
    USER_FRIEND_CANCEL(HttpStatus.NO_CONTENT,"201","친구 취소 완료"),
    USER_FRIEND_REQUEST_ACCEPT(HttpStatus.OK,"200","친구 요청 수락 완료"),
    USER_FRIEND_REQUEST_LIST(HttpStatus.OK,"200","친구 요청 조회 완료"),
    USER_FRIEND_REQUEST_REFUSE(HttpStatus.NO_CONTENT,"201","친구 요청 거절 완료"),
    USER_FRIEND_ARTICLE_LIST(HttpStatus.OK,"200","친구들의 게시글 조회 완료"),
    USER_FRIENDS_LIST(HttpStatus.OK,"200","친구 목록 조회 완료");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public ResponseEntity<CommonResponse> toResponse(Object data){
        return new ResponseEntity<>(CommonResponse.builder()
                .responseCode(this)
                .code(this.code)
                .message(this.message)
                .data(data)
                .build(), httpStatus.OK);
    }
}

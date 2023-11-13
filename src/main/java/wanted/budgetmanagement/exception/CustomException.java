package wanted.budgetmanagement.exception;

public class CustomException extends RuntimeException {
    public CustomException(ErrorCode errorCode) {
        super(errorCode.name() + " : " + errorCode.getMsg());
    }
    public CustomException(ErrorCode errorCode, String str) {
        super(errorCode.name() + " : " + str + errorCode.getMsg());
    }
}

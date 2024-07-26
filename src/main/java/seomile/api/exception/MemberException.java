package seomile.api.exception;

public class MemberException extends IllegalArgumentException {
    // 기본 생성자 추가
    public MemberException() {
        super();
    }

    // 메시지를 받는 생성자
    public MemberException(String message) {
        super(message);
    }

    public static class NicknameDuplicatedException extends MemberException {
        public NicknameDuplicatedException(String message) {
            super(message);
        }
    }

    public static class EmailDuplicatedException extends MemberException {
        public EmailDuplicatedException(String message) {
            super(message);
        }
    }

    public static class AsDescInputException extends MemberException {
        public AsDescInputException(String message) {
            super(message);
        }
    }

    public static class AsPriceInputException extends MemberException {
        public AsPriceInputException(String message) {
            super(message);
        }
    }
}
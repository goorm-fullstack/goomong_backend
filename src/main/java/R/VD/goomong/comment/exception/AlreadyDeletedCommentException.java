package R.VD.goomong.comment.exception;

public class AlreadyDeletedCommentException extends RuntimeException {

    public AlreadyDeletedCommentException() {
        super();
    }

    public AlreadyDeletedCommentException(String message) {
        super(message);
    }
}

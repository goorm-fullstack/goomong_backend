package R.VD.goomong.review.exception;

public class AlreadyDeletedReviewException extends RuntimeException {

    public AlreadyDeletedReviewException() {
        super();
    }

    public AlreadyDeletedReviewException(String message) {
        super(message);
    }
}

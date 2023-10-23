package R.VD.goomong.review.exception;

public class NotFoundReview extends RuntimeException {
    public NotFoundReview() {
        super();
    }

    public NotFoundReview(String message) {
        super(message);
    }
}

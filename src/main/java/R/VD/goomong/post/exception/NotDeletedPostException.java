package R.VD.goomong.post.exception;

public class NotDeletedPostException extends RuntimeException {

    public NotDeletedPostException() {
        super();
    }

    public NotDeletedPostException(String message) {
        super(message);
    }
}

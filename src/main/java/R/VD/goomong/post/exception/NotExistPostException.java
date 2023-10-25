package R.VD.goomong.post.exception;

public class NotExistPostException extends RuntimeException{
    public NotExistPostException() {
        super();
    }

    public NotExistPostException(String message) {
        super(message);
    }
}

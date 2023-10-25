package R.VD.goomong.post.exception;

public class NotDeletedPostCategoryException extends RuntimeException {

    public NotDeletedPostCategoryException() {
        super();
    }

    public NotDeletedPostCategoryException(String message) {
        super(message);
    }
}

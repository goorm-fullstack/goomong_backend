package goomong.item.exception;

public class NotFoundItem extends RuntimeException {
    public NotFoundItem() {
        super();
    }

    public NotFoundItem(String message) {
        super(message);
    }
}

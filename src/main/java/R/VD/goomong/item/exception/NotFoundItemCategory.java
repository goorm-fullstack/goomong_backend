package goomong.item.exception;

public class NotFoundItemCategory extends RuntimeException{
    public NotFoundItemCategory() {
        super();
    }

    public NotFoundItemCategory(String message) {
        super(message);
    }
}

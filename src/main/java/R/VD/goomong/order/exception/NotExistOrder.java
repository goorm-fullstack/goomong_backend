package goomong.order.exception;

public class NotExistOrder extends RuntimeException {
    public NotExistOrder() {
    }

    public NotExistOrder(String message) {
        super(message);
    }
}

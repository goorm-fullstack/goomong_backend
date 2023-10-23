package R.VD.goomong.order.exception;

public class AlreadyCompleteOrder extends RuntimeException {
    public AlreadyCompleteOrder() {
        super();
    }

    public AlreadyCompleteOrder(String message) {
        super(message);
    }
}

package R.VD.goomong.order.exception;

public class InvalidOrderType extends RuntimeException {
    public InvalidOrderType() {
        super();
    }

    public InvalidOrderType(String message) {
        super(message);
    }
}

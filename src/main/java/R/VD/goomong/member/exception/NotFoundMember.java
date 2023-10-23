package R.VD.goomong.member.exception;

public class NotFoundMember extends RuntimeException {
    public NotFoundMember() {
        super();
    }

    public NotFoundMember(String message) {
        super(message);
    }
}

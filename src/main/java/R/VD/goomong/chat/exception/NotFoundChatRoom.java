package R.VD.goomong.chat.exception;

public class NotFoundChatRoom extends RuntimeException {


    public NotFoundChatRoom() {
        super();
    }

    public NotFoundChatRoom(String message) {
        super(message);
    }
}

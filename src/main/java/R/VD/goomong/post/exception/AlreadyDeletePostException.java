package R.VD.goomong.post.exception;

public class AlreadyDeletePostException extends RuntimeException{

    public AlreadyDeletePostException(){
        super();
    }

    public AlreadyDeletePostException(String message){
        super(message);
    }
}

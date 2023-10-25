package R.VD.goomong.post.exception;

public class NotExistPostCategoryInfoException extends RuntimeException{

    public NotExistPostCategoryInfoException(){
        super();
    }

    public NotExistPostCategoryInfoException(String message){
        super(message);
    }
}

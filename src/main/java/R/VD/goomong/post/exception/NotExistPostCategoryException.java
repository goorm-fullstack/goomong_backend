package R.VD.goomong.post.exception;

public class NotExistPostCategoryException extends RuntimeException{

    public NotExistPostCategoryException(){
        super();
    }

    public NotExistPostCategoryException(String message){
        super(message);
    }
}

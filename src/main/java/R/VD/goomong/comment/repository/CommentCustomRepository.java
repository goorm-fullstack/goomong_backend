package R.VD.goomong.comment.repository;

import R.VD.goomong.comment.model.Comment;

public interface CommentCustomRepository {

    void addLikeCount(Comment comment);

    void subLikeCount(Comment comment);
}
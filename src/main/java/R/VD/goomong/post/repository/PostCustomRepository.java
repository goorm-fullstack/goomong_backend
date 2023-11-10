package R.VD.goomong.post.repository;

import R.VD.goomong.post.model.Post;

public interface PostCustomRepository {

    void addLikeCount(Post post);

    void subLikeCount(Post post);
}

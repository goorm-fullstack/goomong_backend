package R.VD.goomong.review.repository;

import R.VD.goomong.review.model.Review;

public interface ReviewCustomRepository {

    void addLikeCount(Review review);

    void subLikeCount(Review review);
}

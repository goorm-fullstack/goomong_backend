package R.VD.goomong.comment.repository;

import R.VD.goomong.comment.model.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static R.VD.goomong.comment.model.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void addLikeCount(Comment selectComment) {
        jpaQueryFactory.update(comment)
                .set(comment.likeNo, comment.likeNo.add(1))
                .where(comment.eq(selectComment))
                .execute();
    }

    @Override
    public void subLikeCount(Comment selectComment) {
        jpaQueryFactory.update(comment)
                .set(comment.likeNo, comment.likeNo.subtract(1))
                .where(comment.eq(selectComment))
                .execute();
    }
}
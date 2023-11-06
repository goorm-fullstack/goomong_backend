package R.VD.goomong.search.repository;

import R.VD.goomong.post.model.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static R.VD.goomong.comment.model.QComment.comment;
import static R.VD.goomong.post.model.QPost.post;
import static R.VD.goomong.post.model.QPostCategory.postCategory;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Post> postSearch(String keyword, String category, String orderBy, Pageable pageable) {
        JPAQuery<Post> query = getPostQuery(keyword, category);

        switch (orderBy) {
            case "title":
                query.orderBy(post.postTitle.asc());
                break;
            case "time":
                query.orderBy(post.regDate.desc());
                break;
            case "views":
                query.orderBy(post.postViews.desc());
                break;
            case "likes":
                query.orderBy(post.postLikeNo.desc());
                break;
            default:
                query.orderBy(post.regDate.desc());
                break;
        }

        List<Post> posts = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = getPostQuery(keyword, category).select(post.count());
        Long total = countQuery.fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    public JPAQuery<Post> getPostQuery(String keyword, String category) {

        JPAQuery<Post> query = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.postCategory, postCategory)
                .leftJoin(post.commentList, comment)
                .where(
                        post.postTitle.contains(keyword)
                                .or(post.postContent.contains(keyword))
                                .or(post.member.memberName.contains(keyword))
                );

        if (category != null && !category.isEmpty()) {
            query.where(post.postCategory.postCategoryName.eq(category));
        }

        return query;
    }
}

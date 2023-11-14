package R.VD.goomong.like.service;

import R.VD.goomong.comment.exception.AlreadyDeletedCommentException;
import R.VD.goomong.comment.exception.NotExistCommentException;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.repository.CommentRepository;
import R.VD.goomong.like.dto.request.RequestCommentLikeDto;
import R.VD.goomong.like.dto.request.RequestPostLikeDto;
import R.VD.goomong.like.dto.request.RequestReviewLikeDto;
import R.VD.goomong.like.exception.DuplicatedLikeException;
import R.VD.goomong.like.exception.NotExistLikeException;
import R.VD.goomong.like.model.Like;
import R.VD.goomong.like.repository.LikeRepository;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import R.VD.goomong.review.exception.AlreadyDeletedReviewException;
import R.VD.goomong.review.exception.NotFoundReview;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    // 게시글 좋아요 추가 기능
    public void likePostInsert(RequestPostLikeDto requestPostHeartDto) {
        Member member = memberRepository.findById(requestPostHeartDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestPostHeartDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Post post = postRepository.findById(requestPostHeartDto.getPostId()).orElseThrow(() -> new NotExistPostException("해당 게시글을 찾을 수 없습니다. id = " + requestPostHeartDto.getPostId()));
        if (post.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + post.getId());

        if (likeRepository.findByMemberAndPost(member, post).isPresent())
            throw new DuplicatedLikeException("해당 회원은 해당 게시글에 좋아요를 클릭한 상태입니다. memberId = " + member.getId() + ", postId = " + post.getId());

        Like build = Like.builder()
                .member(member)
                .post(post)
                .build();
        likeRepository.save(build);
        postRepository.addLikeCount(post);
    }

    // 댓글 좋아요 추가 기능
    public void likeCommentInsert(RequestCommentLikeDto requestCommentLikeDto) {
        Member member = memberRepository.findById(requestCommentLikeDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestCommentLikeDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Comment comment = commentRepository.findById(requestCommentLikeDto.getCommentId()).orElseThrow(() -> new NotExistCommentException("해당 게시글을 찾을 수 없습니다. id = " + requestCommentLikeDto.getCommentId()));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 이미 삭제된 댓글입니다. id = " + comment.getId());

        if (likeRepository.findByMemberAndComment(member, comment).isPresent())
            throw new DuplicatedLikeException("해당 회원은 해당 댓글에 좋아요를 클릭한 상태입니다. memberId = " + member.getId() + ", commentId = " + comment.getId());

        Like build = Like.builder()
                .member(member)
                .comment(comment)
                .build();
        likeRepository.save(build);
        commentRepository.addLikeCount(comment);
    }

    // 리뷰 좋아요 추가 기능
    public void likeReviewInsert(RequestReviewLikeDto requestReviewLikeDto) {
        Member member = memberRepository.findById(requestReviewLikeDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestReviewLikeDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Review review = reviewRepository.findById(requestReviewLikeDto.getReviewId()).orElseThrow(() -> new NotFoundReview("해당 리뷰를 찾을 수 없습니다. id = " + requestReviewLikeDto.getReviewId()));
        if (review.getDelDate() != null)
            throw new AlreadyDeletedReviewException("해당 id의 리뷰는 이미 삭제된 리뷰입니다. id = " + review.getId());

        if (likeRepository.findByMemberAndReview(member, review).isPresent())
            throw new DuplicatedLikeException("해당 회원은 해당 리뷰에 좋아요를 클릭한 상태입니다. memberId = " + member.getId() + ", reviewId = " + review.getId());

        Like build = Like.builder()
                .member(member)
                .review(review)
                .build();
        likeRepository.save(build);
        reviewRepository.addLikeCount(review);
    }

    // 게시글 좋아요 취소 기능
    public void likePostCancel(RequestPostLikeDto requestPostHeartDto) {
        Member member = memberRepository.findById(requestPostHeartDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestPostHeartDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Post post = postRepository.findById(requestPostHeartDto.getPostId()).orElseThrow(() -> new NotExistPostException("해당 게시글을 찾을 수 없습니다. id = " + requestPostHeartDto.getPostId()));
        if (post.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + post.getId());

        Like like = likeRepository.findByMemberAndPost(member, post).orElseThrow(() -> new NotExistLikeException("해당 회원은 해당 게시글에 좋아요를 클릭하지 않았습니다. memberId = " + member.getId() + ", postId = " + post.getId()));
        likeRepository.delete(like);
        postRepository.subLikeCount(post);
    }

    // 댓글 좋아요 취소
    public void likeCommentCancel(RequestCommentLikeDto requestCommentLikeDto) {
        Member member = memberRepository.findById(requestCommentLikeDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestCommentLikeDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Comment comment = commentRepository.findById(requestCommentLikeDto.getCommentId()).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다. id = " + requestCommentLikeDto.getCommentId()));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 이미 삭제된 댓글입니다. id = " + comment.getId());

        Like like = likeRepository.findByMemberAndComment(member, comment).orElseThrow(() -> new NotExistLikeException("해당 회원은 해당 댓글에 좋아요를 클릭하지 않았습니다. memberId = " + member.getId() + ", commentId = " + comment.getId()));
        likeRepository.delete(like);
        commentRepository.subLikeCount(comment);
    }

    // 리뷰 좋아요 취소
    public void likeReviewCancel(RequestReviewLikeDto requestReviewLikeDto) {
        Member member = memberRepository.findById(requestReviewLikeDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestReviewLikeDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 이미 삭제된 회원입니다. id = " + member.getId());

        Review review = reviewRepository.findById(requestReviewLikeDto.getReviewId()).orElseThrow(() -> new NotFoundReview("해당 리뷰를 찾을 수 없습니다. id = " + requestReviewLikeDto.getReviewId()));
        if (review.getDelDate() != null)
            throw new AlreadyDeletedReviewException("해당 id의 리뷰는 이미 삭제된 리뷰입니다. id = " + review.getId());

        Like like = likeRepository.findByMemberAndReview(member, review).orElseThrow(() -> new NotExistLikeException("해당 회원은 해당 리뷰에 좋아요를 클릭하지 않았습니다. memberId = " + member.getId() + ", reviewId = " + review.getId()));
        likeRepository.delete(like);
        reviewRepository.subLikeCount(review);
    }
}

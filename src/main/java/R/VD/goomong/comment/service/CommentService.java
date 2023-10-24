package R.VD.goomong.comment.service;

import R.VD.goomong.comment.dto.request.RequestCommentDto;
import R.VD.goomong.comment.exception.NotExistCommentException;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.repository.CommentRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 댓글 저장
    public void saveComment(RequestCommentDto requestCommentDto) {
        Comment entity = requestCommentDto.toEntity();

        Member member = memberRepository.findById(requestCommentDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        Post post = postRepository.findById(requestCommentDto.getPostId()).orElseThrow(() -> new NotExistPostException("해당 게시글을 찾을 수 없습니다."));

        Comment parent = null;
        if (requestCommentDto.getParentCommentId() != null)
            parent = commentRepository.findById(requestCommentDto.getParentCommentId()).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다."));

        Comment build = entity.toBuilder()
                .member(member)
                .post(post)
                .parentComment(parent)
                .build();
        commentRepository.save(build);
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, RequestCommentDto requestCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다."));

        Comment build = comment.toBuilder()
                .content(requestCommentDto.getContent())
                .build();
        commentRepository.save(build);
        return build;
    }

    // 댓글 소프트 딜리트
    public void softDeleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다."));

        Comment build = comment.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        commentRepository.save(build);
    }

    // 댓글 완전 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }

    // 댓글 좋아요 증가
    public void increaseCommentLike(Long commentId) {
        commentRepository.increaseLikeCount(commentId);
    }

    // 특정 댓글 조회
    public Comment findOneComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 댓글을 찾을 수 없습니다."));
    }

    // 삭제되지 않은 댓글 조회
    public Page<Comment> listOfNotDeletedComment(Pageable pageable) {
        Page<Comment> all = commentRepository.findAll(pageable);
        List<Comment> list = new ArrayList<>();

        for (Comment comment : all) {
            if (comment.getDelDate() == null) list.add(comment);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 댓글 조회
    public Page<Comment> listOfDeletedComment(Pageable pageable) {
        Page<Comment> all = commentRepository.findAll(pageable);
        List<Comment> list = new ArrayList<>();

        for (Comment comment : all) {
            if (comment.getDelDate() != null) list.add(comment);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 댓글 조회
    public Page<Comment> allList(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }
}

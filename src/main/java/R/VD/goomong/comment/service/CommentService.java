package R.VD.goomong.comment.service;

import R.VD.goomong.comment.dto.request.RequestCommentDto;
import R.VD.goomong.comment.exception.AlreadyDeletedCommentException;
import R.VD.goomong.comment.exception.NotDeletedCommentException;
import R.VD.goomong.comment.exception.NotExistCommentException;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.repository.CommentRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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

        Member member = memberRepository.findById(requestCommentDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원을 찾을 수 없습니다. id = " + requestCommentDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());

        Post post = postRepository.findById(requestCommentDto.getPostId()).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + requestCommentDto.getPostId()));
        if (post.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 글입니다. id = " + post.getId());

        Comment parent = null;
        if (requestCommentDto.getParentCommentId() != null) {
            parent = commentRepository.findById(requestCommentDto.getParentCommentId()).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + requestCommentDto.getParentCommentId()));
            if (parent.getDelDate() != null)
                throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + parent.getId());
        }

        Comment build = entity.toBuilder()
                .member(member)
                .post(post)
                .parentComment(parent)
                .build();
        commentRepository.save(build);
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, RequestCommentDto requestCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + commentId));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + comment.getId());

        Comment build = comment.toBuilder()
                .content(requestCommentDto.getContent())
                .build();
        return commentRepository.save(build);
    }

    // 댓글 소프트 딜리트
    public void softDeleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + commentId));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + comment.getId());

        Comment build = comment.toBuilder()
                .delDate(ZonedDateTime.now())
                .build();
        commentRepository.save(build);
    }

    // 댓글 완전 삭제
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + commentId));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + comment.getId());
        commentRepository.delete(comment);
    }

    // 삭제된 댓글 복구
    public void unDelete(Long commentId) {
        Comment origin = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + commentId));
        if (origin.getDelDate() == null)
            throw new NotDeletedCommentException("해당 id의 댓글은 삭제된 댓글이 아닙니다. id = " + origin.getId());

        Comment build = origin.toBuilder()
                .delDate(null)
                .build();
        commentRepository.save(build);
    }

    // 특정 댓글 조회
    public Comment findOneComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글을 찾을 수 없습니다. id = " + commentId));
        if (comment.getDelDate() != null)
            throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + commentId);
        return comment;
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

package R.VD.goomong.post.service;

import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotDeletedPostException;
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
public class PostService {

    private final PostRepository postRepository;

    // 게시글 소프트딜리트
    public void softDeletePost(Long postId) {
        Post onePost = findOnePost(postId);
        if (onePost.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + postId);

        Post build = onePost.toBuilder()
                .delDate(ZonedDateTime.now())
                .build();
        postRepository.save(build);
    }

    // 게시글 완전 삭제
    public void deletePost(Long postId) {
        Post onePost = findOnePost(postId);
        if (onePost.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + postId);
        postRepository.delete(onePost);
    }

    // 삭제된 게시글 복구
    public void unDeleted(Long postId) {
        Post origin = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글은 없습니다. id = " + postId));
        if (origin.getDelDate() == null) throw new NotDeletedPostException("해당 id의 게시글은 삭제된 글이 아닙니다. id = " + postId);

        Post build = origin.toBuilder()
                .delDate(null)
                .build();
        postRepository.save(build);
    }

    // 조회수 증가
    public void increaseViewCount(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + postId));
        if (post.getDelDate() != null) throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 게시글입니다. id = " + postId);
        postRepository.increaseViewCount(postId);
    }

    // 하나의 게시글 조회
    public Post findOnePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + postId));
        if (post.getDelDate() != null) throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 게시글입니다. id = " + postId);
        return post;
    }

    // 삭제되지 않은 게시글 조회
    public Page<Post> listOfNotDeleted(Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();
        for (Post post : all) {
            if (post.getDelDate() == null) list.add(post);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 게시글 조회
    public Page<Post> listOfDeleted(Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();
        for (Post post : all) {
            if (post.getDelDate() != null) list.add(post);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    // 게시글 종류로 조회(판매/기부/교환/커뮤니티/FAQ)
    public Page<Post> listOfType(String type, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        for (Post post : all) {
            if (post.getPostType().equals(type)) list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 게시글 카테고리로 조회(커뮤니티 및 FAQ에서 사용)
    public Page<Post> listOfCategory(String category, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        for (Post post : all) {
            if (post.getPostCategory().equals(category)) list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 게시글 조회
    public Page<Post> allList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}

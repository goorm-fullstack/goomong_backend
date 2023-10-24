package R.VD.goomong.post.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.dto.request.RequestPostDto;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostCategoryException;
import R.VD.goomong.post.exception.NotExistPostCategoryInfoException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.model.PostCategory;
import R.VD.goomong.post.repository.PostCategoryRepository;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final ImageService imageService;

    // 게시글 생성
    public void savePost(RequestPostDto requestPostDto, MultipartFile[] postImages, MultipartFile[] postFiles) {
        Post entity = requestPostDto.toEntity();

        Member writer = memberRepository.findById(requestPostDto.getMemberId()).orElseThrow(() -> new RuntimeException("찾을 수 없는 회원입니다."));

        String postType = entity.getPostType();
        if ((postType.equals("커뮤니티") || postType.equals("FAQ")) && requestPostDto.getPostCategoryId() == null)
            throw new NotExistPostCategoryInfoException("카테고리를 선택해주세요");

        Item item = null;
        if (requestPostDto.getItemId() != null)
            item = itemRepository.findById(requestPostDto.getItemId()).orElseThrow(() -> new NotFoundItem("해당 상품이 없습니다."));

        PostCategory postCategory = null;
        if (requestPostDto.getPostCategoryId() != null)
            postCategory = postCategoryRepository.findById(requestPostDto.getPostCategoryId()).orElseThrow(() -> new NotExistPostCategoryException("해당 카테고리가 없습니다."));

        List<Image> postImageList = entity.getImageList();
        if (postImages.length != 0) postImageList = imageService.saveImage(postImages);

        List<Image> postFileList = entity.getFileList();
        if (postFiles.length != 0) postFileList = imageService.saveImage(postFiles);

        Post dbg = entity.toBuilder()
                .member(writer)
                .item(item)
                .postCategory(postCategory)
                .imageList(postImageList)
                .fileList(postFileList)
                .build();
        postRepository.save(dbg);
    }

    // 게시글 수정
    public Post updatePost(Long postId, RequestPostDto requestPostDto, MultipartFile[] postImages, MultipartFile[] postFiles) {
        Post onePost = findOnePost(postId);

        List<Image> postImageList = null;
        if (postImages.length != 0) postImageList = imageService.saveImage(postImages);

        List<Image> postFileList = null;
        if (postFiles.length != 0) postFileList = imageService.saveImage(postFiles);

        Post build = onePost.toBuilder()
                .postTitle(requestPostDto.getPostTitle())
                .postContent(requestPostDto.getPostContent())
                .imageList(postImageList)
                .fileList(postFileList)
                .chgDate(LocalDateTime.now())
                .build();
        postRepository.save(build);

        return build;
    }

    // 게시글 소프트딜리트
    public void softDeletePost(Long postId) {
        Post onePost = findOnePost(postId);
        if (onePost.getDelDate() != null) throw new AlreadyDeletePostException("이미 삭제된 게시글입니다.");

        Post build = onePost.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        postRepository.save(build);
    }

    // 게시글 완전 삭제
    public void deletePost(Long postId) {
        Post onePost = findOnePost(postId);
        postRepository.delete(onePost);
    }

    // 조회수 증가
    public void increaseViewCount(Long postId) {
        postRepository.increaseViewCount(postId);
    }

    // 좋아요 증가
    public void increaseLikeCount(Long postId) {
        postRepository.increaseLikeCount(postId);
    }

    // 하나의 게시글 조회
    public Post findOnePost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 게시글을 찾을 수 없습니다."));
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

    // 전체 게시글 조회
    public Page<Post> allList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}

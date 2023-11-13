package R.VD.goomong.post.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.dto.request.RequestPostDto;
import R.VD.goomong.post.exception.*;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.model.Type;
import R.VD.goomong.post.repository.CategoryRepository;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final FilesService filesService;

    // 게시글 생성
    public void savePost(RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files) {

        Post entity = requestPostDto.toEntity();

        Member member = memberRepository.findById(requestPostDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestPostDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());

        Category category = null;
        if (requestPostDto.getPostCategoryId() != null) {
            category = categoryRepository.findById(requestPostDto.getPostCategoryId()).orElseThrow(() -> new NotExistCategoryException("해당 id의 카테고리를 찾을 수 없습니다. id = " + requestPostDto.getPostCategoryId()));
            if (category.getDelDate() != null)
                throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 이미 삭제된 카테고리입니다. id = " + category.getId());
        }

        Type type = Type.EVENT;
        type = type.toType(requestPostDto.getPostType());

        List<Image> imageList = entity.getImageList();
        if (images.length != 0) imageList = imageService.saveImage(images);

        List<Files> fileList = entity.getFileList();
        if (files.length != 0) fileList = filesService.saveFiles(files);

        Post build = entity.toBuilder()
                .member(member)
                .postCategory(category)
                .postType(type)
                .imageList(imageList)
                .fileList(fileList)
                .build();
        postRepository.save(build);
    }

    // 게시글 수정
    public Post updatePost(Long postId, RequestPostDto requestPostDto, MultipartFile[] images, MultipartFile[] files) {
        Post origin = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + postId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + postId);

        Category originCategory = origin.getPostCategory();
        if (!requestPostDto.getPostCategoryId().equals(originCategory.getId())) {
            originCategory = categoryRepository.findById(requestPostDto.getPostCategoryId()).orElseThrow(() -> new NotExistCategoryException("해당 id의 카테고리를 찾을 수 없습니다. id = " + requestPostDto.getPostCategoryId()));
            if (originCategory.getDelDate() != null)
                throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 이미 삭제된 카테고리입니다. id = " + originCategory.getId());
        }

        Type type = Type.EVENT;
        type = type.toType(requestPostDto.getPostType());

        List<Image> imageList = origin.getImageList();
        if (images.length != 0) imageList = imageService.saveImage(images);

        List<Files> filesList = origin.getFileList();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Post build = origin.toBuilder()
                .postCategory(originCategory)
                .postTitle(requestPostDto.getPostTitle())
                .postContent(requestPostDto.getPostContent())
                .postType(type)
                .fileList(filesList)
                .imageList(imageList)
                .build();
        return postRepository.save(build);
    }

    // 게시글 소프트딜리트
    public void softDeletePost(Long postId) {
        Post onePost = findOnePost(postId);
        if (onePost.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + postId);

        Post build = onePost.toBuilder()
                .delDate(LocalDateTime.now())
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

    // 삭제되지 않은 게시글 중 게시글 종류로 조회(커뮤니티/공지사항/이벤트)
    public Page<Post> listOfNotDeletedAndType(String type, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        Type t = Type.COMMUNITY;
        t = t.toType(type);

        for (Post post : all) {
            if (post.getPostType().equals(t) && post.getDelDate() == null) list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 게시글 중 게시글 종류로 조회(커뮤니티/공지사항/이벤트)
    public Page<Post> listOfDeletedAndType(String type, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        Type t = Type.COMMUNITY;
        t = t.toType(type);

        for (Post post : all) {
            if (post.getPostType().equals(t) && post.getDelDate() != null) list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 게시글 종류로 전체 리스트 조회(커뮤니티/공지사항/이벤트)
    public Page<Post> listOfAllAndType(String type, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        Type t = Type.COMMUNITY;
        t = t.toType(type);

        for (Post post : all) {
            if (post.getPostType().equals(t)) list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제되지 않은 게시글 중 게시글 카테고리로 조회(커뮤니티)
    public Page<Post> listOfNotDeletedAndCategory(String category, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        for (Post post : all) {
            if (post.getPostCategory().getCategoryName().equals(category) && post.getDelDate() == null)
                list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 게시글 중 게시글 카테고리로 조회(커뮤니티)
    public Page<Post> listOfDeletedAndCategory(String categoryName, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        for (Post post : all) {
            if (post.getPostCategory().getCategoryName().equals(categoryName) && post.getDelDate() != null)
                list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 게시글 카테고리로 전체 리스트 조회(커뮤니티)
    public Page<Post> listOfAllAndCategory(String categoryName, Pageable pageable) {
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> list = new ArrayList<>();

        for (Post post : all) {
            if (post.getPostCategory().getCategoryName().equals(categoryName))
                list.add(post);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 게시글 조회
    public Page<Post> allList(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}

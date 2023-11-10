package R.VD.goomong.post.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.dto.request.RequestItemPostDto;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.exception.NotUnavailableTypeException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemPostService {

    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final FilesService filesService;

    // 판매/기부/교환 게시글 생성
    public void saveItemPost(RequestItemPostDto requestItemPostDto, MultipartFile[] postImages, MultipartFile[] postFiles) {
        if (requestItemPostDto.getPostType().equals("커뮤니티") || requestItemPostDto.getPostType().equals("FAQ"))
            throw new NotUnavailableTypeException("해당 게시글에 사용할 수 없는 type입니다. type = " + requestItemPostDto.getPostType());

        Post entity = requestItemPostDto.toEntity();

        Member writer = memberRepository.findById(requestItemPostDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원은 없습니다. id = " + requestItemPostDto.getMemberId()));
        if (writer.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + writer.getId());

        Item item = null;
        if (requestItemPostDto.getItemId() != null) {
            item = itemRepository.findById(requestItemPostDto.getItemId()).orElseThrow(() -> new NotFoundItem("해당 id의 상품이 없습니다. id = " + requestItemPostDto.getItemId()));
            if (item.getDelDate() != null) throw new RuntimeException("해당 id의 상품은 삭제된 상품입니다. id = " + item.getId());
        }

        List<Image> postImageList = entity.getImageList();
        if (postImages.length != 0) postImageList = imageService.saveImage(postImages);

        List<Files> postFileList = entity.getFileList();
        if (postFiles.length != 0) postFileList = filesService.saveFiles(postFiles);

        Post dbg = entity.toBuilder()
                .member(writer)
                .item(item)
                .imageList(postImageList)
                .fileList(postFileList)
                .build();
        postRepository.save(dbg);
    }

    // 판매/기부/교환 게시글 수정
    public Post updateItemPost(Long postId, RequestItemPostDto requestItemPostDto, MultipartFile[] postImages, MultipartFile[] postFiles) {
        Post onePost = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + postId));
        if (onePost.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 게시글입니다. id = " + onePost.getId());

        if (requestItemPostDto.getPostType().equals("커뮤니티") || requestItemPostDto.getPostType().equals("FAQ"))
            throw new NotUnavailableTypeException("해당 게시글에 사용할 수 없는 type입니다. type = " + requestItemPostDto.getPostType());

        List<Image> postImageList = onePost.getImageList();
        if (postImages.length != 0) postImageList = imageService.saveImage(postImages);

        List<Files> postFileList = onePost.getFileList();
        if (postFiles.length != 0) postFileList = filesService.saveFiles(postFiles);

        Post build = onePost.toBuilder()
                .postTitle(requestItemPostDto.getPostTitle())
                .postContent(requestItemPostDto.getPostContent())
                .imageList(postImageList)
                .fileList(postFileList)
                .build();
        return postRepository.save(build);
    }
}

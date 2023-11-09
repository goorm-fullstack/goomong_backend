package R.VD.goomong.post.service;

import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.dto.request.RequestFaqCommunityPostDto;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqCommunityPostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final FilesService filesService;

    // FAQ, 커뮤니티 게시글 생성
    public void saveFaqCommunityPost(RequestFaqCommunityPostDto requestFaqCommunityPostDto, MultipartFile[] images, MultipartFile[] files) {
        Post entity = requestFaqCommunityPostDto.toEntity();

        Member member = memberRepository.findById(requestFaqCommunityPostDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 id의 회원을 찾을 수 없습니다. id = " + requestFaqCommunityPostDto.getMemberId()));
        if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());

        List<Image> imageList = entity.getImageList();
        if (images.length != 0) imageList = imageService.saveImage(images);

        List<Files> fileList = entity.getFileList();
        if (files.length != 0) fileList = filesService.saveFiles(files);

        Post build = entity.toBuilder()
                .member(member)
                .imageList(imageList)
                .fileList(fileList)
                .build();
        postRepository.save(build);
    }

    // FAQ, 커뮤니티 게시글 수정
    public Post updateFaqCommunityPost(Long postId, RequestFaqCommunityPostDto requestFaqCommunityPostDto, MultipartFile[] images, MultipartFile[] files) {
        Post origin = postRepository.findById(postId).orElseThrow(() -> new NotExistPostException("해당 id의 게시글을 찾을 수 없습니다. id = " + postId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletePostException("해당 id의 게시글은 이미 삭제된 게시글입니다. id = " + postId);

        List<Image> imageList = origin.getImageList();
        if (images.length != 0) imageList = imageService.saveImage(images);

        List<Files> filesList = origin.getFileList();
        if (files.length != 0) filesList = filesService.saveFiles(files);

        Post build = origin.toBuilder()
                .postTitle(requestFaqCommunityPostDto.getPostTitle())
                .postContent(requestFaqCommunityPostDto.getPostContent())
                .fileList(filesList)
                .imageList(imageList)
                .build();
        return postRepository.save(build);
    }
}

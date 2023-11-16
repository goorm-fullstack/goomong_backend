package R.VD.goomong.review.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.review.dto.request.RequestReviewDto;
import R.VD.goomong.review.exception.AlreadyDeletedReviewException;
import R.VD.goomong.review.exception.NotFoundReview;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.repository.ReviewRepository;
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
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ImageService imageService;

    // 리뷰 저장
    public void save(RequestReviewDto requestReviewDto, MultipartFile[] multipartFiles) {
        Review entity = requestReviewDto.toEntity();

        Member member = memberRepository.findById(requestReviewDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원을 찾을 수 없습니다. id = " + requestReviewDto.getMemberId()));
        if (member.getDelDate() != null)
            throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + requestReviewDto.getMemberId());

        Item target = itemRepository.findById(requestReviewDto.getItemId()).orElseThrow(() -> new NotFoundItem("해당 id의 상품을 찾을 수 없습니다. id = " + requestReviewDto.getItemId()));
        if (target.getDelDate() != null)
            throw new RuntimeException("해당 id의 상품은 이미 삭제된 상품입니다. id = " + requestReviewDto.getItemId());

        List<Image> imageList = null;
        if (multipartFiles.length != 0) imageList = imageService.saveImage(multipartFiles);

        Review build = entity.toBuilder()
                .member(member)
                .item(target)
                .imageList(imageList)
                .build();
        reviewRepository.save(build);
    }

    //리뷰 수정
    public Review updateReview(Long reviewId, RequestReviewDto requestReviewDto, MultipartFile[] images) {
        Review origin = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundReview("해당 id의 리뷰를 찾을 수 없습니다. id = " + reviewId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedReviewException("해당 id의 리뷰는 이미 삭제된 리뷰입니다. id = " + reviewId);

        List<Image> imageList = origin.getImageList();
        if (images.length != 0) imageList = imageService.saveImage(images);

        Review build = origin.toBuilder()
                .title(requestReviewDto.getTitle())
                .content(requestReviewDto.getContent())
                .imageList(imageList)
                .rate(requestReviewDto.getRate())
                .build();
        return reviewRepository.save(build);
    }

    // 리뷰 삭제(소프트딜리트)
    public void deleteReview(Long reviewId) {
        Review origin = reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundReview("해당 id의 리뷰를 찾을 수 없습니다. id = " + reviewId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedReviewException("해당 id의 리뷰는 이미 삭제된 리뷰입니다. id = " + reviewId);

        Review build = origin.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        reviewRepository.save(build);
    }

    // 특정 리뷰 조회
    public Review findById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundReview("대상을 찾을 수 없습니다"));
        if (review.getDelDate() != null) throw new AlreadyDeletedReviewException("해당 id의 리뷰는 이미 삭제된 리뷰입니다. id = " + id);

        return review;
    }

    // 삭제되지 않은 리뷰 조회
    public Page<Review> listOfNotDeleted(Pageable pageable) {
        Page<Review> all = reviewRepository.findAll(pageable);
        List<Review> list = new ArrayList<>();

        for (Review review : all) {
            if (review.getDelDate() == null) list.add(review);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 리뷰 조회
    public Page<Review> listOfDeleted(Pageable pageable) {
        Page<Review> all = reviewRepository.findAll(pageable);
        List<Review> list = new ArrayList<>();

        for (Review review : all) {
            if (review.getDelDate() != null) list.add(review);
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 리뷰 조회
    public Page<Review> allList(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    // MemberId로 리뷰 조회
    public Page<Review> findReviewsByMemberId(Long id, Pageable pageable){
        Page<Review> all = reviewRepository.findAllByMemberId(id, pageable);
        List<Review> list = new ArrayList<>();

        for(Review review : all){
            if(review.getDelDate() != null)
                list.add(review);
        }

        return new PageImpl<>(list, pageable, list.size());
    }
}

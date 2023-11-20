package R.VD.goomong.review.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.review.dto.request.RequestReviewDto;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import R.VD.goomong.review.exception.AlreadyDeletedReviewException;
import R.VD.goomong.review.exception.NotFoundReview;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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
        if (multipartFiles != null) imageList = imageService.saveImage(multipartFiles);

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
        if (images != null) imageList = imageService.saveImage(images);

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

    // 전체 리뷰 평균 평점 구하기
    public String aveReview() {
        List<Review> all = reviewRepository.findAll();

        if (all.size() > 0) {
            float result = 0;
            for (Review review : all) {
                if (review.getDelDate() == null) result += review.getRate();
            }

            log.info("ave={}", String.format("%.1f", result / all.size()));

            return String.format("%.1f", result / all.size());
        }
        return String.valueOf(0);
    }

    // 고객 만족도 구하기
    public String customerSatisfaction() {
        float ave = Float.parseFloat(aveReview());
        float total = 5.0F;
        log.info("ave={}, total={}, ave/total={}, result={}", ave, total, ave / total, (ave / total) * 100);
        return String.format("%.1f", (ave / total) * 100);
    }

    // 베스트 후기 구하기
    public Page<ResponseReviewDto> bestReview(int pageNumber, int pageSize) {
        Sort sort = Sort.by(
                Sort.Order.desc("rate"),
                Sort.Order.desc("likeNo"),
                Sort.Order.desc("commentNo")
        );
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        List<ResponseReviewDto> all = reviewRepository.findAll().stream().map(Review::toResponseReviewDto).toList();

        return new PageImpl<>(all, pageRequest, all.size());
    }
}

package goomong.review.service;

import goomong.image.model.Image;
import goomong.image.service.ImageService;
import goomong.item.exception.NotFoundItem;
import goomong.item.model.Item;
import goomong.item.repository.ItemRepository;
import goomong.review.dto.request.RequestReviewDto;
import goomong.review.dto.response.ResponseReviewDto;
import goomong.review.exception.NotFoundReview;
import goomong.review.model.Review;
import goomong.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final ImageService imageService;

    public void save(Long itemId, RequestReviewDto requestReviewDto, MultipartFile[] multipartFiles) {
        Item target = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundItem("존재하지 않는 아이템입니다."));
        List<Image> imageList = imageService.saveImage(multipartFiles);
        Review review = requestReviewDto.toEntity();
        review.setItem(target);
        review.setImageList(imageList);

        Review save = reviewRepository.save(review);
        target.getReviewList().add(save);
        target.calculateRate();
    }

    public List<ResponseReviewDto> findAll() {
        return reviewRepository.findAll().stream().map(ResponseReviewDto::new).toList();
    }

    public ResponseReviewDto findById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new NotFoundReview("대상을 찾을 수 없습니다"));
        return new ResponseReviewDto(review);
    }
}

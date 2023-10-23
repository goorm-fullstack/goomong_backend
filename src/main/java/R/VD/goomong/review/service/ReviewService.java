package R.VD.goomong.review.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.exception.NotFoundItem;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.repository.ItemRepository;
import R.VD.goomong.review.dto.request.RequestReviewDto;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import R.VD.goomong.review.exception.NotFoundReview;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.repository.ReviewRepository;
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

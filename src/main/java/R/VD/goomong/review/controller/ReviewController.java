package goomong.review.controller;

import goomong.review.dto.request.RequestReviewDto;
import goomong.review.dto.response.ResponseReviewDto;
import goomong.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/save/{itemId}")
    public ResponseEntity<String> writeReview(
            @PathVariable Long itemId,
            @RequestPart RequestReviewDto requestReviewDto,
            MultipartFile[] multipartFiles) {
        reviewService.save(itemId, requestReviewDto, multipartFiles);
        return ResponseEntity.ok("작성 완료");
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseReviewDto>> getList() {
        List<ResponseReviewDto> reviewList = reviewService.findAll();
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReviewDto> getById(@PathVariable Long id) {
        ResponseReviewDto review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }
}

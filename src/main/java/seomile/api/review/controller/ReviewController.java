package seomile.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.review.dto.ReviewDTO;
import seomile.api.review.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 관광지 리뷰 조회
    @GetMapping("/{travCode}")
    public ResponseEntity<List<ReviewDTO>> getTravelList(@PathVariable("travCode") String travCode) {
        List<ReviewDTO> reviews = reviewService.getTravelReview(travCode);
        return ResponseEntity.ok(reviews);
    }

    // 관광지 리뷰 작성
    @PostMapping(value = "/{travCode}")
    public ResponseEntity<String> writeReview(@PathVariable("travCode") String travCode, @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(reviewService.writeReview(travCode, reviewDTO), HttpStatus.OK);
    }

    // 관광지 리뷰 수정
    @PatchMapping(value = "/{travCode}/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable("reviewId") Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        return (new ResponseEntity<>(reviewService.updateReview(reviewId, reviewDTO), HttpStatus.OK));
    }

    // 관광지 리뷰 삭제
    @DeleteMapping(value = "/{travCode}/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId) throws Exception {
        return new ResponseEntity<>(reviewService.deleteReview(reviewId), HttpStatus.OK);
    }


}

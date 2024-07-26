package seomile.api.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.review.dto.ReviewDTO;
import seomile.api.review.entity.Review;
import seomile.api.review.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{travCode}")
    public ResponseEntity<List<Review>> getTravelList(@PathVariable("travCode") String travCode) {
        List<Review> reviews = reviewService.getTravelReview(travCode);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping(value = "/{travCode}")
    public ResponseEntity<String> writeReview(@PathVariable("travCode") String travCode, @RequestBody ReviewDTO reviewDTO) throws Exception {
        return new ResponseEntity<>(reviewService.writeReview(travCode, reviewDTO), HttpStatus.OK);
    }

}

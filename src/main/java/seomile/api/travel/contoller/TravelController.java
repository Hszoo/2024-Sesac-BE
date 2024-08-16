package seomile.api.travel.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.review.dto.ReviewDTO;
import seomile.api.review.service.ReviewService;
import seomile.api.travel.dto.TravelDTO;
import seomile.api.travel.dto.TravelListDTO;
import seomile.api.travel.service.TravelService;

import java.util.List;

@RestController
@RequestMapping("/travel")
public class TravelController {

    @Autowired
    private TravelService travelService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/list")
    public List<TravelListDTO> getTravelList(@RequestParam(required = false) List<Integer> categories) {
        return travelService.fetchTravelInfoByCategory(categories);
    }

    @GetMapping("")
    public TravelDTO getTravelList(@RequestParam(required = true) String travelCode) {
        return travelService.fetchTravelDetailInfo(travelCode);
    }

    //관광지별 해당 리뷰 조회
    @GetMapping("/{travCode}/review/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewByTravelCodeAndReviewId(@PathVariable("travCode") String travCode, @PathVariable("reviewId") Long reviewId) {
        ReviewDTO review = reviewService.getReviewByTravelCodeAndReviewId(travCode, reviewId);
        return ResponseEntity.ok(review);
    }

    //관광지 검색
    @GetMapping("/search")
    public List<TravelDTO> searchTravel(@RequestParam String keyword) {
        return travelService.searchTravelByKeyword(keyword);
    }

}
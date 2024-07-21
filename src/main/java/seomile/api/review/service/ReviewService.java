package seomile.api.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seomile.api.review.dto.ReviewDTO;
import seomile.api.review.entity.Review;
import seomile.api.review.repository.ReviewRepository;
import seomile.api.travel.entity.Travel;
import seomile.api.travel.repository.TravelRepository;
import seomile.api.travel.service.TravelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TravelRepository travelRepository;
    private final TravelService travelService;

    @Transactional
    public String writeReview(String travCode, ReviewDTO reviewDTO) {
        // TRAVEL 엔티티를 찾거나 없으면 생성
        Travel travel = travelService.findOrCreateTravel(travCode);

        // REVIEW 엔티티 생성 및 설정
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setTravel(travel);
        review.setMember(reviewDTO.getMember());
        review.setDate(LocalDate.now());
        review.setStartDate(reviewDTO.getStartDate());
        review.setEndDate(reviewDTO.getEndDate());
        review.setRate(reviewDTO.getRate());

        // REVIEW 엔티티 저장
        reviewRepository.save(review);

        // 성공 메시지 반환
        return reviewDTO.getTitle() + " 리뷰 작성 완료";
    }

    public List<Review> getTravelReview(String travCode) {
        // travCode로 여행지 찾기
        Travel travel = travelRepository.findByTravCode(travCode)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found"));

        // 여행지에 대한 리뷰 찾기
        return reviewRepository.findByTravelCode(travel.getTravCode());
    }
}
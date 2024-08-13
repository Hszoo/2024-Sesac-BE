package seomile.api.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seomile.api.member.entity.Member;
import seomile.api.member.repository.MemberRepository;
import seomile.api.review.dto.ReviewDTO;
import seomile.api.review.entity.Review;
import seomile.api.review.repository.ReviewRepository;
import seomile.api.security.SecurityUtils;
import seomile.api.travel.entity.Travel;
import seomile.api.travel.repository.TravelRepository;
import seomile.api.travel.service.TravelService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TravelRepository travelRepository;
    private final TravelService travelService;
    private final MemberRepository memberRepository;

    @Transactional
    public String writeReview(String travCode, ReviewDTO reviewDTO) {
        // TRAVEL 엔티티를 찾거나 없으면 생성
        Travel travel = travelService.findOrCreateTravel(travCode);

        // 현재 로그인한 사용자의 정보 가져오기
        String userName = SecurityUtils.getCurrentMemberId()
                .orElseThrow(() -> new RuntimeException("사용자가 인증되지 않았습니다."));

        // 사용자 정보를 Optional<Member>로 가져오기
        Member member = memberRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // REVIEW 엔티티 생성 및 설정
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setTravel(travel);
        review.setMember(member); // 수정된 부분
        review.setDate(LocalDate.now());
        review.setStartDate(reviewDTO.getStartDate());
        review.setEndDate(reviewDTO.getEndDate());
        review.setRate(reviewDTO.getRate());

        // REVIEW 엔티티 저장
        reviewRepository.save(review);

        // 성공 메시지 반환
        return reviewDTO.getTitle() + " 리뷰 작성 완료";
    }

    public List<ReviewDTO> getTravelReview(String travCode) {
//        // travCode로 여행지 찾기
//        Travel travel = travelRepository.findByTravCode(travCode)
//                .orElseThrow(() -> new IllegalArgumentException("Travel not found"));

        // 여행지에 대한 리뷰 찾기
        List<Review> reviews = reviewRepository.findByTravelCode(travCode);

        // Review 리스트를 ReviewDTO 리스트로 변환
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return reviewDTOs;
    }
    @Transactional
    public String updateReview(Long reviewId, ReviewDTO reviewDTO) {
        // 현재 로그인한 사용자의 정보 가져오기
        String userName = SecurityUtils.getCurrentMemberId()
                .orElseThrow(() -> new RuntimeException("사용자가 인증되지 않았습니다."));

        // 수정할 리뷰 찾기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자가 현재 사용자와 일치하는지 확인
        if (!review.getMember().getUsername().equals(userName)) {
            throw new RuntimeException("리뷰 수정 권한이 없습니다.");
        }

        // 리뷰 정보 업데이트
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        review.setStartDate(reviewDTO.getStartDate());
        review.setEndDate(reviewDTO.getEndDate());
        review.setRate(reviewDTO.getRate());

        // 리뷰 저장
        reviewRepository.save(review);

        return reviewDTO.getTitle() + " 리뷰 수정 완료";
    }

    @Transactional
    public String deleteReview(Long reviewId) {
        // 현재 로그인한 사용자의 정보 가져오기
        String userName = SecurityUtils.getCurrentMemberId()
                .orElseThrow(() -> new RuntimeException("사용자가 인증되지 않았습니다."));
        // 기존 리뷰 찾기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자가 현재 사용자와 일치하는지 확인
        if (!review.getMember().getUsername().equals(userName)) {
            throw new RuntimeException("리뷰 삭제 권한이 없습니다.");
        }

        // 리뷰 삭제
        reviewRepository.delete(review);

        return "리뷰 삭제 완료";
    }
    
    //리뷰아이디, 여행지코드로 리뷰 찾기
    public ReviewDTO getReviewByTravelCodeAndReviewId(String travCode, Long reviewId) {
        Review review = reviewRepository.findByTravelCodeAndReviewId(travCode, reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        return new ReviewDTO(review);
    }

}
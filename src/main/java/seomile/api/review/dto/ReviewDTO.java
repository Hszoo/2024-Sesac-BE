package seomile.api.review.dto;

import java.time.LocalDate;

import lombok.*;
import seomile.api.member.dto.MemberResponseDTO;
import seomile.api.member.entity.Member;
import seomile.api.review.entity.Review;
import seomile.api.travel.entity.Travel;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private Travel travel;
    private MemberResponseDTO member;
    private String title;
    private String content;
    private LocalDate date; // 작성일자
    private LocalDate startDate; // 여행지 방문 기간 (시작)
    private LocalDate endDate; // 여행지 방문 기간 (끝)
    private Double rate;

    public ReviewDTO(Travel travel, MemberResponseDTO member, String title, String content,
                     LocalDate startDate, LocalDate endDate, Double rate) {
        this.travel = travel;
        this.member = member;
        this.title = title;
        this.content = content;
        this.date = LocalDate.now();
        this.startDate = startDate;
        this.endDate = endDate;
        this.rate = rate;
    }

    public ReviewDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.travel = review.getTravel();
        this.member = new MemberResponseDTO(review.getMember()); // 필요 시 MemberResponseDTO에 변환자 추가
        this.title = review.getTitle();
        this.content = review.getContent();
        this.date = review.getDate();
        this.startDate = review.getStartDate();
        this.endDate = review.getEndDate();
        this.rate = review.getRate();
    }
}
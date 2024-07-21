package seomile.api.review.dto;

import java.time.LocalDate;

import lombok.*;
import seomile.api.member.dto.MemberResponseDTO;
import seomile.api.member.entity.Member;
import seomile.api.travel.entity.Travel;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private Travel travel;
    private Member member;
    private String title;
    private String content;
    private LocalDate date; // 작성일자
    private LocalDate startDate; // 여행지 방문 기간 (시작)
    private LocalDate endDate; // 여행지 방문 기간 (끝)
    private Double rate;

    public ReviewDTO(Travel travel, Member member, String title, String content,
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
}
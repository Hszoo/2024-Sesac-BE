package seomile.api.review.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import seomile.api.travel.entity.Travel;
import seomile.api.member.entity.Member;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "trav_id")
    @JsonManagedReference // 무한 루프 방지를 위해 추가
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    private String title;
    private String content;
    private LocalDate date;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double rate;
}

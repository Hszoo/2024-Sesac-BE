package seomile.api.review.entity;

import jakarta.persistence.*;
import seomile.api.travel.entity.Travel;
import seomile.api.member.entity.Member;

import java.util.Date;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "travId")
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;

    private String reviewTitle;
    private Date startDate;
    private Date endDate;
    private String rate;

}

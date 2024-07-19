package seomile.api.saveTravel.entity;

import jakarta.persistence.*;
import seomile.api.travel.entity.Travel;
import seomile.api.member.entity.Member;

@Entity
@Table(name = "SaveTravel")
public class SaveTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "travId")
    private Travel travel;
}
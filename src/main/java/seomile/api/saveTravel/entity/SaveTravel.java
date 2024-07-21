package seomile.api.saveTravel.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import seomile.api.travel.entity.Travel;
import seomile.api.member.entity.Member;
@Getter
@Setter
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
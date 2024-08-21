package seomile.api.travel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import seomile.api.review.entity.Review;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Travel")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travId;

    private String travCode;

    @OneToMany(mappedBy = "travel")
    @JsonIgnore // 무한 루프 방지를 위해 추가
    private List<Review> reviews;

}

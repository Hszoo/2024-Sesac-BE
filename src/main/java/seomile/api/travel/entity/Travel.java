package seomile.api.travel.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Travel")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travId;

    private String travelName;
    private String travAddress;
    private String travTel;
    private Date closeDate;
    private String availableTime;
    private String fee;
    private String travImg;
}

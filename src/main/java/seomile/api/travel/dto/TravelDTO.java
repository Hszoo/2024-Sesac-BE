package seomile.api.travel.dto;

import java.util.Date;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDTO {
    private Long travId;
    private String travelName;
    private String travAddress;
    private String travTel;
    private Date closeDate;
    private String availableTime;
    private String fee;
    private String travImg;
}
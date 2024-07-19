package seomile.api.review.dto;

import java.util.Date;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewId;
    private Long travId;
    private Long userId;
    private String reviewTitle;
    private Date startDate;
    private Date endDate;
    private String rate;
}
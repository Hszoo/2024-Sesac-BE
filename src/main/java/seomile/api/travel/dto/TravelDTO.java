package seomile.api.travel.dto;

import java.util.Date;
import java.util.List;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDTO {
    private Long travId;
    private String travName;
    private String travAddress;
    private String travImg;
    private List<String> travCategories; // 지체, 시각 등 카테고리 저장
    private Integer reviewCount;
    private Integer reviewRating;
    private String tel;
    private Date closeDate;
    private String availableTime;
    private String fee;

    // 추가 정보를 어떤 거 받아올지 정해보자능..

}
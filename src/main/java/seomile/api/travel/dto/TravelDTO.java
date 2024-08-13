package seomile.api.travel.dto;

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
    private String travX;
    private String travY;
    private String travImg;
    private List<String> travCategories; // 지체, 시각 등 카테고리 저장
    private Integer reviewCount;
    private Integer reviewRating;
    private String tel;
    private String closeDate;
    private String availableTime;
    private String fee;

    public TravelDTO(String travName, String travAddress, String travImg, String tel, String closeDate,
                     String availableTime, String fee) {
        this.travName = travName;
        this.travAddress = travAddress;
        this.travImg = travImg;
        this.tel = tel;
        this.closeDate = closeDate;
        this.availableTime = availableTime;
        this.fee = fee;
    }

}
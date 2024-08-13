package seomile.api.travel.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelListDTO {
    private String travId;
    private String travName;
    private String travAddress;
    private String travX;
    private String travY;

    private Double reviewRate; // 소수점 몇째 짜리 까지 표시할지 정해야
    private List<String>  travCategories; // 지체, 시각 등 카테고리 저장
    private String travImg;

    public TravelListDTO(String travId, String travName, String travAddress, String travImg) {
        this.travId = travId;
        this.travName = travName;
        this.travAddress = travAddress;
        this.travImg = travImg;
    }
}

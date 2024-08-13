package seomile.api.travel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {

    private Meta meta;
    private List<Document> documents;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private int total_count;
        private int pageable_count;
        @JsonProperty("is_end")
        private boolean is_end;
    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {
        @JsonProperty("address_name")
        private String addressName;
        @JsonProperty("address_type")
        private String addressType;
        @JsonProperty("road_address")
        private RoadAddress roadAddress;
        private String x;
        private String y;
        // address field added
        private Address address;

        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Address {
            @JsonProperty("address_name")
            private String addressName;
            private String b_code;
            private String h_code;
            private String main_address_no;
            private String mountain_yn;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_h_name;
            private String region_3depth_name;
            private String sub_address_no;
            private String x;
            private String y;
        }
        @Builder
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RoadAddress {
            @JsonProperty("address_name")
            private String addressName;
            private String building_name;
            private String main_building_no;
            private String sub_building_no;
            private String region_1depth_name;
            private String region_2depth_name;
            private String region_3depth_name;
            private String road_name;
            private String zone_no;
            private String x;
            private String y;
        }
    }
}
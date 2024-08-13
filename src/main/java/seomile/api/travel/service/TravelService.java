package seomile.api.travel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import seomile.api.travel.dto.GeoLocation;
import seomile.api.travel.dto.TravelDTO;
import seomile.api.travel.dto.TravelListDTO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import seomile.api.travel.entity.Travel;
import seomile.api.travel.repository.TravelRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelService {
    @Value("${kakao.api.key}")
    private String geoKey;

    @Autowired
    private TravelRepository travelRepository;

    public Travel findOrCreateTravel(String travCode) {
        Travel travel = travelRepository.findByTravCode(travCode)
                .orElseGet(() -> createNewTravel(travCode));
        System.out.println("Travel found or created: " + travel);
        return travel;
    }

    public Travel createNewTravel(String travCode) {
        Travel newTravel = new Travel();
        newTravel.setTravCode(travCode);
        travelRepository.save(newTravel);
        System.out.println("New Travel created and saved: " + newTravel);
        return newTravel;
    }

    // base URL
    String domain = "https://www.seouldanurim.net";
    String basePath = "/attractions/D/TOURINFOTYPE2";


    // 여행지 정보 카테고리 별 크롤링
    public List<TravelListDTO> fetchTravelInfoByCategory(List<Integer> categories) {
        List<TravelListDTO> travelInfoList = new ArrayList<>();

        // 카테고리 기반으로 URL 생성
        String url = domain + basePath + generateUrl(categories) + "&srchTypeCd=5&srchTypeCd=6&srchTypeCd=7";
        System.out.println("요청 url : " + url);

        try {
            Document doc = Jsoup.connect(url).get();

            // 하나의 관광지 요소가 li 태그로 반복됨
            Elements travelElements = doc.select("li.card");

            // li태그의 각 요소를 순회 -> 정보 추출
            for (Element travelElement : travelElements) {
                // 여행지 ID값 추출 -> 여행지 상세 조회시 사용
                Element linkElement = travelElement.select("a").first();
                String travelId = extractAttractionId(linkElement);

                // 관광지명 추출
                Element travelNameElement = travelElement.select("h5.text-overflow").first();
                String travelName = travelNameElement != null ? travelNameElement.text() : "Not Found";

                // 주소 추출
                Element addressElement = travelElement.select("p").first();
                String address = addressElement != null ? addressElement.text() : "Not Found";

                // 이미지 URL 추출
                Element imageElement = travelElement.select("span.bg-thumb").first();
                String styleAttribute = imageElement != null ? imageElement.attr("style") : "Not Found";
                String imageUrl = domain + extractImageUrl(styleAttribute);


                TravelListDTO travelInfo = new TravelListDTO(travelId, travelName, address, imageUrl);

                // geocoding : 주소 -> X, Y 좌표
                addressToXY(travelInfo, address);

                travelInfoList.add(travelInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return travelInfoList;
    }


    // 상세 관광지 정보 크롤링
    public TravelDTO fetchTravelDetailInfo(String travelID) {
        // 관광지 ID로 URL 생성
        String url = domain + basePath + "/" + travelID;
        System.out.println("요청 url : " + url);

        TravelDTO travelDetailInfo = null;
        try {
            Document doc = Jsoup.connect(url).get();

            // 관광지명 추출
            Element travelNameElement = doc.select("h1").first();
            String travelName = travelNameElement != null ? travelNameElement.text() : "Not Found";

            // 주소 추출
            Element addressElement = doc.select("div.table.has-map > div > p").first();
            String address = addressElement != null ? addressElement.text() : "Not Found";

            // 이미지 요소 선택
            Elements imgElements = doc.select("div.item.slick-slide.slick-current.slick-active > img");

            if (imgElements.isEmpty()) {
                System.out.println("No image elements found. Please check the selector.");
            }

            String fullImgSrc = "";
            for (Element imgElement : imgElements) {
                String imgSrc = imgElement.attr("src");
                fullImgSrc = domain + imgSrc;

                // 결과 출력
                System.out.println("Image src: " + fullImgSrc);
            }

            // 전화번호 추출
            String tel = getTableData(doc, "전화번호");

            // 휴무일 추출
            String closeDate = getTableData(doc, "휴무일");

            // 이용가능 시간 추출
            String availableTime = getTableData(doc, "이용시간");

            // 이용 요금 추출
            String fee = getTableData(doc, "이용요금");

            travelDetailInfo = new TravelDTO(travelName, address, fullImgSrc, tel, closeDate, availableTime, fee);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return travelDetailInfo;
    }

    public List<TravelDTO> searchTravelByKeyword(String keyword) {
        List<TravelListDTO> travelInfoList = fetchTravelInfoByCategory(new ArrayList<>()); // 빈 카테고리 리스트 전달
        List<TravelDTO> result = new ArrayList<>();

        for (TravelListDTO travel : travelInfoList) {
            if (travel.getTravName().toLowerCase().contains(keyword.toLowerCase())) {
                TravelDTO detailInfo = fetchTravelDetailInfo(travel.getTravId());
                if (detailInfo != null) {
                    result.add(detailInfo);
                }
            }
        }
        return result;
    }

    // 카테고리 번호에 따라 URL 생성
    private String generateUrl(List<Integer> categories) {
        String addUrl = "?sortOrder=&srchType=all&srchFilter=&srchWord=";
        StringBuilder urlBuilder = new StringBuilder(addUrl);

        if(categories == null) {
            urlBuilder.append("&srchWeakTypeCd=").append("all");
        } else {
            for (Integer category : categories) {
                String categoryCode = getCategoryCode(category);
                if (!categoryCode.isEmpty()) {
                    urlBuilder.append("&srchWeakTypeCd=").append(categoryCode);
                }
            }
        }
        return urlBuilder.toString();
    }

    // 요청 받은 카테고리 번호 -> 코드 변환
    private String getCategoryCode(Integer category) {
        return "TOUR_WEAK_TYPE_" + category;
    }

    // 테이블에서 특정 항목의 데이터 추출
    private String getTableData(Document doc, String title) {
        Elements tableElements = doc.select("div.table");
        for (Element tableElement : tableElements) {
            Element titleElement = tableElement.select("div.title > strong").first();
            if (titleElement != null && title.equals(titleElement.text())) {
                return tableElement.select("div").last().text();
            }
        }
        return "Not Found";
    }

    // HTML에서 이미지 URL 추출
    private String extractImageUrl(String styleAttribute) {
        String urlPrefix = "background-image:url('";
        String urlSuffix = "')";
        int startIndex = styleAttribute.indexOf(urlPrefix) + urlPrefix.length();
        int endIndex = styleAttribute.indexOf(urlSuffix);
        return styleAttribute.substring(startIndex, endIndex);
    }

    // 관광지의 ID값 추출
    private String extractAttractionId(Element linkElement) {
        if (linkElement != null) {
            String href = linkElement.attr("href");
            String[] parts = href.split("/");
            if (parts.length >= 4) {
                return parts[4]; // ID는 4번째 부분
            }
        }
        return "Not Found";
    }

    // 지오코딩할 주소 전달 받음
    private void addressToXY(TravelListDTO travelDto, String address) {

        // geocoding : X, Y 좌표값 받아옴
        ResponseEntity<String> response = requestKakaoGeocoder(address);

        // 응답을 파싱
        parsingLocation(travelDto, response);
    }

    // 카카오 API 요청
    private ResponseEntity<String> requestKakaoGeocoder(String address) {
        // URL에 쿼리 파라미터 추가
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + geoKey);
        headers.set("content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // json Return
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
    }

    // parsing : 카카오 API 응답 -> X,Y좌표값
    private TravelListDTO parsingLocation(TravelListDTO travelDto, ResponseEntity<String> kakaoJson) {
        try {
            // JSON 응답
            String responseBody = kakaoJson.getBody();

            // JSON -> 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            GeoLocation apiResponse = objectMapper.readValue(responseBody, GeoLocation.class);

            // x, y 좌표 parsing
            if (apiResponse.getDocuments() != null && !apiResponse.getDocuments().isEmpty()) {
                GeoLocation.Document document = apiResponse.getDocuments().get(0);
                System.out.println("주소 : " + document.getAddressName() + " X 좌표 : " + document.getX() + " Y 좌표 : " + document.getY());

                travelDto.setTravX(document.getX());
                travelDto.setTravY(document.getY());

                return travelDto;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return travelDto;
    }
}
package seomile.api.travel.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


}
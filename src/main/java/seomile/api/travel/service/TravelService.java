package seomile.api.travel.service;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import seomile.api.travel.dto.TravelListDTO;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TravelService {

    // 여행지 정보를 크롤링하여 리스트로 반환하는 메소드
    public List<TravelListDTO> fetchTravelInfo(List<Integer> categories) {
        List<TravelListDTO> travelInfoList = new ArrayList<>();

        // 카테고리 기반으로 URL 생성
        String url = generateUrl(categories);
        System.out.println("요청 url : " + url);

        try {
            Document doc = Jsoup.connect(url).get();

            // class="card" 의 li 요소 -> 하나의 관광지
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
                String imageUrl = extractImageUrl(styleAttribute);

                TravelListDTO travelInfo = new TravelListDTO(travelId, travelName, address, imageUrl);
                travelInfoList.add(travelInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return travelInfoList;
    }

    // 카테고리 번호에 따라 URL 생성
    private String generateUrl(List<Integer> categories) {
        String baseUrl = "https://www.seouldanurim.net/attractions/D/TOURINFOTYPE2?sortOrder=&srchType=all&srchFilter=&srchWord=";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

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
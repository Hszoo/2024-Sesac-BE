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
    public List<TravelListDTO> fetchTravelInfo() {
        List<TravelListDTO> travelInfoList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.seouldanurim.net/index").get();

            // class="card" 의 li 요소 -> 하나의 관광지
            Elements travelElements = (Elements) doc.select("li.card");

            // 각 요소를 순회하면서 정보를 추출합니다
            for (Element travelElement : travelElements) {
                // 여행지 ID값 추출 -> 여행지 상세 조회시 사용
                Element linkElement = travelElement.select("a").first();
                String travelId = extractAttractionId(linkElement);

                // 여행지 이름 추출
                Element travelNameElement = travelElement.select("h5.text-overflow").first();
                String travelName = travelNameElement != null ? travelNameElement.text() : "Not Found";

                // 주소 추출
                Element addressElement = travelElement.select("p").first();
                System.out.println("Card Dsc HTML: " + (addressElement != null ? addressElement.html() : "null"));
                String address = addressElement != null ? addressElement.text() : "Not Found";

                // 이미지 URL 추출
                Element imageElement = travelElement.select("span.bg-thumb").first();
                String styleAttribute = imageElement != null ? imageElement.attr("style") : "Not Found";
                String imageUrl = extractImageUrl(styleAttribute);

                // 추출한 정보를 TravelInfo 객체에 담아 리스트에 추가합니다
                TravelListDTO travelInfo = new TravelListDTO(travelId, travelName, address, imageUrl);
                travelInfoList.add(travelInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return travelInfoList;
    }

    // HTML에서 이미지 URL 추출하는 메서드
    private String extractImageUrl(String styleAttribute) {
        String urlPrefix = "background-image:url('";
        String urlSuffix = "')";
        int startIndex = styleAttribute.indexOf(urlPrefix) + urlPrefix.length();
        int endIndex = styleAttribute.indexOf(urlSuffix);
        return styleAttribute.substring(startIndex, endIndex);
    }

    // 여행지 ID값 추출하는 메서드
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
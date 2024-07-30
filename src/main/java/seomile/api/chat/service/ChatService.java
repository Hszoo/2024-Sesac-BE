package seomile.api.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seomile.api.chat.dto.ChatMessageDTO;
import seomile.api.chat.dto.ChatResponseDTO;
import seomile.api.travel.dto.TravelDTO;
import seomile.api.travel.dto.TravelListDTO;
import seomile.api.travel.service.TravelService;
import seomile.api.gpt.service.GptService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private TravelService travelService;

    @Autowired
    private GptService gptService;


    private List<ChatMessageDTO> messages = new ArrayList<>();
    private List<ChatResponseDTO> responses = new ArrayList<>();

    // 모든 메시지 가져오기
    public List<ChatMessageDTO> getAllMessages() {
        return messages;
    }

    // 특정 사용자의 메시지 가져오기
    public List<ChatMessageDTO> getMessagesBySender(String sender) {
        return messages.stream()
                .filter(message -> message.getSender().equals(sender))
                .collect(Collectors.toList());
    }

    //gpt 응답 가져오기
    public List<ChatResponseDTO> getAllResponses() {
        return responses;
    }

    // 메시지 처리
    public ChatResponseDTO processMessage(ChatMessageDTO chatMessageDTO) {
        chatMessageDTO = new ChatMessageDTO(
                chatMessageDTO.getSender(),
                chatMessageDTO.getRecipientBot(),
                chatMessageDTO.getMessage()
        );
        messages.add(chatMessageDTO);
        String gptResponse = getGptResponse(chatMessageDTO.getMessage());
        ChatResponseDTO response = new ChatResponseDTO(
                "ChatBot", chatMessageDTO.getSender(), gptResponse, LocalDateTime.now()
        );

        responses.add(response);

        return response;
    }

    //GPT 응답 메시지 활용
    private String getGptResponse(String userMessage) {
        List<TravelListDTO> travelListDTOs = travelService.fetchTravelInfoByCategory(new ArrayList<>());
        List<TravelDTO> travelList = travelListDTOs.stream().map(this::convertToTravelDTO).collect(Collectors.toList());


        String prompt = "여행지 목록이다:\n";
        for (TravelDTO travel : travelList) {
            prompt += travel.getTravName() + " - " + travel.getTravAddress() + "\n";
        }
        prompt += "\n사용자에게 맞는 여행지 추천: " + userMessage;

        for (ChatMessageDTO message : messages) {
            prompt += message.getSender() + ": " + message.getMessage() + "\n";
        }
        String gptResponse = gptService.callGptApi(prompt);
        return gptResponse;
    }

    private TravelDTO convertToTravelDTO(TravelListDTO travelListDTO) {
        return new TravelDTO(
                travelListDTO.getTravName(),
                travelListDTO.getTravAddress(),
                travelListDTO.getTravImg(),
                "전화번호 정보 없음", // 값이 없을때 기본값
                "휴무일 정보 없음",   //
                "이용 가능 시간 정보 없음", //
                "이용 요금 정보 없음" //
        );
    }

    //

}
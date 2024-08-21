package seomile.api.socket;

import java.util.List;
import java.util.Map;

import com.corundumstudio.socketio.AckRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;
import seomile.api.chat.dto.ChatMessageDTO;
import seomile.api.chat.dto.ChatResponseDTO;
import seomile.api.chat.service.ChatService;

/**
 * SocketIOController.
 */
@Component
@Slf4j
public class SocketIOController {
    private final ChatService chatService;

    public SocketIOController(SocketIOServer server, ChatService chatService) {
        this.chatService = chatService;
        // 소켓 이벤트 리스너 등록
        server.addConnectListener(listenConnected());
        server.addDisconnectListener(listenDisconnected());

        // 채팅 메시지 이벤트 리스너 등록
        server.addEventListener("chat_message", ChatMessageDTO.class, this::handleChatMessage);
    }

    public ConnectListener listenConnected() {
        return (client) -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            log.info("Client connected: " + params.toString());
        };
    }

    public DisconnectListener listenDisconnected() {
        return client -> {
            String sessionId = client.getSessionId().toString();
            log.info("Client disconnected: " + sessionId);
            client.disconnect();
        };
    }

    // 채팅 메시지를 처리하는 메서드
    private void handleChatMessage(com.corundumstudio.socketio.SocketIOClient client, ChatMessageDTO data, AckRequest ackRequest) {
        log.info("Received chat message: " + data);

        // 메시지를 처리하고 응답을 전송하는 서비스 호출
        ChatResponseDTO response = chatService.processMessage(data);
        client.sendEvent("chat_response", response);
    }
}
package seomile.api.socket;

import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * SocketIoServerLifeCycle.
 *
 * <p>
 *   SocketIoServer의 생명주기를 관리
 * </p>
 */
@Component
public class SocketIOServerLifeCycle {
    private final SocketIOServer server;

    public SocketIOServerLifeCycle(SocketIOServer server) {
        this.server = server;
    }

    /**
     * SocketIo 서버 시작
     */
    @PostConstruct
    public void start() {
        server.start();
    }

    /**
     * SocketIo 서버 종료
     */
    @PreDestroy
    public void stop() {
        server.stop();
    }
}
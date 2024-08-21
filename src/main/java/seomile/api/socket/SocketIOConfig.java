    package seomile.api.socket;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    import com.corundumstudio.socketio.SocketIOServer;

    @Configuration
    public class SocketIOConfig {

        @Value("${socketio.server.hostname}")
        private String hostname;

        @Value("${socketio.server.port}")
        private int port;


        // netty 서버 생성

        @Bean
        public SocketIOServer socketIoServer() {
            com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
            config.setHostname(hostname);
            config.setPort(port);
            return new SocketIOServer(config);
        }
    }
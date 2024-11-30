package naegamaja_server.naegamaja.system.configuration;

import naegamaja_server.naegamaja.domain.game.websocket.ControlWebSocketHandler;
import naegamaja_server.naegamaja.domain.game.websocket.GameWebsocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration(proxyBeanMethods = false)
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final GameWebsocketHandler gameWebSocketHandler;
    private final ControlWebSocketHandler controlWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/ws").setAllowedOrigins("*");
        registry.addHandler(controlWebSocketHandler, "/control").setAllowedOrigins("*");
    }
}

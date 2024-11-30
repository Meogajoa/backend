package naegamaja_server.naegamaja.domain.game.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class ControlWebSocketHandler extends TextWebSocketHandler {

    private final GameWebsocketHandler gameWebsocketHandler;

    HashMap<String, Connection> connections = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        connections.put(session.getId(), Connection.of(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        connections.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)  {
        String payload = message.getPayload();

        gameWebsocketHandler.sendMessageToAll(payload);
    }

}

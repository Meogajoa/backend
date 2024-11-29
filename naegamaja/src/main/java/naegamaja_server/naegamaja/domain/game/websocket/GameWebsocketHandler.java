package naegamaja_server.naegamaja.domain.game.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameWebsocketHandler extends TextWebSocketHandler {
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
        //String payload = message.getPayload();

        //GameInfoDto.Request request = objectMapper.readValue(payload, GameInfoDto.Request.class);
        List<Connection> receiverConnections = connections.values().stream().toList();

        receiverConnections.forEach(connection -> {
            try{
                connection.getSession().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void sendMessageToAll(String message){
        connections.values().forEach(connection -> {
            try{
                connection.getSession().sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

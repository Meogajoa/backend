package naegamaja_server.naegamaja.domain.game.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.GameStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
public class StreamingWebSocketHandler extends TextWebSocketHandler {
    HashMap<String, Connection> connections = new HashMap<>();
    private final GameStatus gameStatus;
    private final ObjectMapper objectMapper;

public StreamingWebSocketHandler(GameStatus gameStatus, ObjectMapper objectMapper) {
        this.gameStatus = gameStatus;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        connections.put(session.getId(), Connection.of(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        connections.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        connections.values().forEach(connection -> {
            try {
                connection.getSession().sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendBoard(){
        try{
            connections.values().forEach(connection -> {
                try{
                    connection.getSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(gameStatus)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

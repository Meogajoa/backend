package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.websocket.GameWebsocketHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    private final GameWebsocketHandler gameWebsocketHandler;

    public void sendMessageToAll(String message) {
        gameWebsocketHandler.sendMessageToAll(message);
    }
}

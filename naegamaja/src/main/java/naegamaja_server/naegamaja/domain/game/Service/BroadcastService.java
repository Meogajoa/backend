package naegamaja_server.naegamaja.domain.game.Service;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.websocket.StreamingWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BroadcastService {

    private final StreamingWebSocketHandler streamingWebSocketHandler;

    @Scheduled(fixedRate =200)
    public void broadcast() {
        streamingWebSocketHandler.sendBoard();
    }

}

package naegamaja_server.naegamaja.domain.game.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.Service.GameService;
import naegamaja_server.naegamaja.system.websocket.dto.NaegamajaMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameStompController {

    private final GameService gameService;

    @MessageMapping("/game/{gameId}")
    public void startGame(@DestinationVariable String gameId, @Header("Authorization") String sessionId, @Payload NaegamajaMessage.Request message) {

        gameService.test(gameId, sessionId, message);
    }
}

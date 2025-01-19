package naegamaja_server.naegamaja.domain.game.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.Service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameStompController {

    private final GameService gameService;

    @MessageMapping("/game/{gameId}/start")
    public void startGame(@DestinationVariable String gameId) {
        //gameService.startGame(gameId);
    }
}

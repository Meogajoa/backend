package naegamaja_server.naegamaja.domain.game.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.Service.GameService;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.domain.session.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GameRestController {

    private final GameService gameService;
    private final RoomService roomService;
    private final SessionService sessionService;

    @PostMapping("/game/{gameId}/start")
    public ResponseEntity<?> startGame(@PathVariable String gameId, @RequestHeader("Authorization") String sessionId) {
        gameService.startGame(gameId, sessionId);
        return ResponseEntity.ok().build();
    }
}

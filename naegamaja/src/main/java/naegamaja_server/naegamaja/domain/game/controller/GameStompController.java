package naegamaja_server.naegamaja.domain.game.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.service.RedisStreamChatPublisher;
import naegamaja_server.naegamaja.domain.game.Service.GameService;
import naegamaja_server.naegamaja.system.websocket.dto.MeogajoaMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameStompController {

    private final GameService gameService;
    private final RedisStreamChatPublisher redisStreamChatPublisher;

    @MessageMapping("/game/{gameId}/chat")
    public void chat(@DestinationVariable String gameId, @Header("Authorization") String authorization, @Payload MeogajoaMessage.Request message) {

        //chatLogService.roomChat(roomNumber, message, authorization);
        redisStreamChatPublisher.publishGameChatMessage(gameId, message, authorization);
    }

    @MessageMapping("/game/{gameId}")
    public void game(@DestinationVariable String gameId, @Header("Authorization") String authorization, @Payload MeogajoaMessage.Request message) {
        gameService.test(gameId, authorization, message);
    }

    @MessageMapping("/game/{gameId}/buttonClick")
    public void buttonClick(@DestinationVariable String gameId, @Header("Authorization") String authorization, @Payload MeogajoaMessage.Request message) {
        gameService.buttonClick(gameId, authorization, message);
    }



}

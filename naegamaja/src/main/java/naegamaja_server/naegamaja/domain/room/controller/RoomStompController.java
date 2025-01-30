package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.chat.service.RedisStreamChatPublisher;
import naegamaja_server.naegamaja.system.websocket.dto.NaegamajaMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoomStompController {

    private final RedisStreamChatPublisher redisStreamChatPublisher;


    @MessageMapping("/room/{roomNumber}/chat")
    public void chat(@DestinationVariable String roomNumber, @Header("Authorization") String authorization, @Payload NaegamajaMessage.Request message) {

        //chatLogService.roomChat(roomNumber, message, authorization);
        redisStreamChatPublisher.publishRoomChatMessage(roomNumber, message, authorization);

        //todo
    }
}

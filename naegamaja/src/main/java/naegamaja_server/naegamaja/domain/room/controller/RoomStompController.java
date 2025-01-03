package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.service.RoomService;
import naegamaja_server.naegamaja.system.websocket.dto.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RoomStompController {

    private final RoomService roomService;


    @MessageMapping("/room/{roomNumber}/chat")
    public void chat(@DestinationVariable Long roomNumber, @Header("Authorization") String authorization,@Payload Message.Request message) {
        System.out.println(message.getContent());

        roomService.chat(roomNumber, message, authorization);

        //todo
    }
}

package naegamaja_server.naegamaja.domain.room.controller;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.room.dto.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RoomStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/room/{roomNumber}/chat")
    public void chat(@DestinationVariable Long roomNumber, Message message) {
        System.out.println("roomNumber = " + roomNumber);
    }
}

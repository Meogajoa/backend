package naegamaja_server.naegamaja.domain.game;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/app/1")
    @SendTo("/topic/1")
    public String game(String message) {
        return message;
    }
}

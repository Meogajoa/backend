package naegamaja_server.naegamaja;

import lombok.RequiredArgsConstructor;
import naegamaja_server.naegamaja.domain.game.GameStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;
    private final GameStatus gameStatus;

    @GetMapping("/사랑해")
    public String 사랑해(){
        return "index";
    }

    @GetMapping("/잘자용")
    public String 잘자용(){
        return "goodnight";
    }

    @GetMapping(value = "/test0", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String test(){
        helloService.sendMessageToAll("날라옴0");
        return "1";
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String test(@RequestParam(value = "value", required = false, defaultValue = "-1") int value){
        if(value != -1){
            helloService.sendMessageToAll("Received value: " + value);
            return "Received value: " + value;
        } else {
            helloService.sendMessageToAll("No value received");
            return "No value received";
        }
    }

    @GetMapping("/tempo")
    public String tempo(){
        return "tempo";
    }

    @GetMapping("/tempo2")
    public String tempo2(){
        return "tempo2";
    }

    @GetMapping("/streamingtest")
    public String streamingtest(){
        return "streamingtest";
    }

    @GetMapping("/up")
    @ResponseBody
    public void up(){
        gameStatus.up();
    }

    @GetMapping("/down")
    @ResponseBody
    public void down(){
        gameStatus.down();
    }

    @GetMapping("/left")
    @ResponseBody
    public void left(){
        gameStatus.left();
    }

    @GetMapping("/right")
    @ResponseBody
    public void right(){
        gameStatus.right();
    }
}

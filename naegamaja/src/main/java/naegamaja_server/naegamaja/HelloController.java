package naegamaja_server.naegamaja;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final HelloService helloService;

    @GetMapping("/사랑해")
    public String 사랑해(){
        return "index";
    }

    @GetMapping("/잘자용")
    public String 잘자용(){
        return "goodnight";
    }

    @GetMapping("/test")
    public void test(){
        helloService.sendMessageToAll("날라옴");
    }

    @GetMapping("/tempo")
    public String tempo(){
        return "tempo";
    }

    @GetMapping("/tempo2")
    public String tempo2(){
        return "tempo2";
    }


}

package naegamaja_server.naegamaja;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "안녕 배종찬 서버\nㅋㅋ";
    }

    @GetMapping("/")
    public String go(){
        return "Test입니당";
    }


}

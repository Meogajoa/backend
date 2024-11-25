package naegamaja_server.naegamaja;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {

    @GetMapping("/사랑해")
    public String 사랑해(){
        return "index";
    }

    @GetMapping("/잘자용")
    public String 잘자용(){
        return "goodnight";
    }


}

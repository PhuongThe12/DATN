package luckystore.datn.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String getAccessDenied(){
        return "/accessDenied";
    }

    @GetMapping("/not-found")
    public String getNotFound(){
        return "/notFound";
    }

}

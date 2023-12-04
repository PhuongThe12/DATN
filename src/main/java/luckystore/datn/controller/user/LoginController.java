package luckystore.datn.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/authentication")
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "/user/login1";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/user/signup";
    }
}

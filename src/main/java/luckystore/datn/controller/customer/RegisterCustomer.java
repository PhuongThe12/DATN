package luckystore.datn.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterCustomer {
    @RequestMapping("/register")
    public String register(){
        return "/customer/register";
    }
}

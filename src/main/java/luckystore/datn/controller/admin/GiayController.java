package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/giay")
public class GiayController {

    @GetMapping()
    public String getIndex() {
        return "/admin/giay/index";
    }

    @GetMapping("/test")
    public String test() {
        return "/admin/giay/test";
    }

}

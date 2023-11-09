package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/ban-hang")
public class BanHangController {

    @GetMapping
    public String getIndex(){
        return "/admin/banhang/index";
    }
}

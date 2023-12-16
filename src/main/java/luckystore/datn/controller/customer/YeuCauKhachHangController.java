package luckystore.datn.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/yeu-cau")
public class YeuCauKhachHangController {

    @GetMapping
    public String getIndex(){
        return "/user/yeucau/index";
    }
}

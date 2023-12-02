package luckystore.datn.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/khach-hang")
public class ThongTinTaiKhoanController {
    @GetMapping()
    public String getIndex() {
        return "/user/thongtintaikhoan/index";
    }
}

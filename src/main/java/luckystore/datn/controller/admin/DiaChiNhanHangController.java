package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khach-hang/dia-chi-nhan-hang")
public class DiaChiNhanHangController {
    @GetMapping()
    public String getIndex() {
        return "/admin/diachinhanhang/index";
    }
}

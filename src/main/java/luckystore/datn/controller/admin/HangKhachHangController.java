package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/hang-khach-hang")
public class HangKhachHangController {
    @GetMapping()
    public String getIndex() {
        return "/admin/hangkhachhang/index";
    }
}

package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/thong-ke")
public class ThongKeController {

    @GetMapping()
    public String getIndex() {
        return "/admin/thongke/index";
    }
}

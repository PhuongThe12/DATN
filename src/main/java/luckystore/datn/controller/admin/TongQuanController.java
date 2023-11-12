package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tong-quan")
public class TongQuanController {
    @GetMapping()
    public String getIndex() {
        return "/admin/tongquan/index";
    }
}

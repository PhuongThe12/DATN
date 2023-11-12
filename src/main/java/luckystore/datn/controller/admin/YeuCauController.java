package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/yeu-cau")
public class YeuCauController {
    @GetMapping()
    public String getIndex() {
        System.out.println("requets");
        return "/admin/yeucau/index";
    }
}

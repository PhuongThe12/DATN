package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/mau-sac")
public class MauSacController {

    @GetMapping()
    public String getIndex() {
        return "/admin/mausac/index";
    }

}

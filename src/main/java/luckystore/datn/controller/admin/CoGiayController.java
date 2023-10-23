package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/co-giay")
public class CoGiayController {

    @GetMapping()
    public String getIndex() {
        return "/admin/cogiay/index";
    }

}
package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/mui-giay")
public class MuiGiayController {

    @GetMapping()
    public String getIndex() {
        return "/admin/muigiay/index";
    }

}

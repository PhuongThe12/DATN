package luckystore.datn.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/don-hang")
public class DonHangController {
    @GetMapping()
    public String getIndex() {
        return "/admin/donhang/index";
    }
}

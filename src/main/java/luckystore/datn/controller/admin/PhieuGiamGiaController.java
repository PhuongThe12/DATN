package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {

    @GetMapping()
    public String getIndex() {
        return "/admin/phieugiamgia/index";
    }
}

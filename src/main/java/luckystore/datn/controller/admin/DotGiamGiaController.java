package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dot-giam-gia")
public class DotGiamGiaController {

    @GetMapping()
    public String getIndex() {
        return "/admin/dotgiamgia/index";
    }

}

package luckystore.datn.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khach-hang/san-pham-yeu-thich")
public class SanPhamYeuThichController {
    @GetMapping()
    public String getIndex() {
        return "/user/yeuthich/index";
    }
}

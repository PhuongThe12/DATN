package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/image/upload")
public class UploadImageController {

    @GetMapping()
    public String getIndex() {
        return "admin/upload/index";
    }

    @GetMapping("/test")
    public String test() {
        return "admin/test";
    }
}

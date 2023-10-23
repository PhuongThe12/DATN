package luckystore.datn.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/chat-lieu")
public class ChatLieuController {

    @GetMapping()
    public String getIndex() {
        return "/admin/chatlieu/index";
    }

}
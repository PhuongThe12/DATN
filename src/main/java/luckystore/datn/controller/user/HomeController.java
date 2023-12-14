package luckystore.datn.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.NhanVien;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

//    private final HttpServletRequest request;
    @GetMapping("home")
    public String login(){
//        HttpSession session = request.getSession();
//        NhanVien user = (NhanVien) session.getAttribute("employee");
//        if (user != null) {
//            return "/user/login1";
//        }
        return "/index";
    }
}

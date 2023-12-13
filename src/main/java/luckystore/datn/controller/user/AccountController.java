package luckystore.datn.controller.user;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import luckystore.datn.service.user.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/confirm/{id}/{token}")
    @SneakyThrows
    public String confirm(@PathVariable("id") Long id, @PathVariable("token") String token, Model model) {
        String status = accountService.confirm(id, token);
        model.addAttribute("message", status);
        return "confirm";
    }

    @GetMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id") Long id, @RequestParam("token") String token, Model model) {
        String status = accountService.resetPassword(id, token);
        model.addAttribute("message", status);
        return "confirm";
    }


}

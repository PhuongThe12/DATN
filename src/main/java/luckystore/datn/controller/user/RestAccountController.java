package luckystore.datn.controller.user;

import com.google.gson.Gson;
import luckystore.datn.service.user.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/account")
public class RestAccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("email") String email) {
        accountService.confirm(email);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    @GetMapping("/forgot-password") //forgot-password?email=
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        accountService.forgotPassword(email);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }

    @GetMapping("/khach-hang")
    public ResponseEntity<?> find(@RequestParam("email") String email, @RequestParam("sdt")String sdt) {
        return ResponseEntity.ok(new Gson().toJson(accountService.find(email, sdt)));
    }

}

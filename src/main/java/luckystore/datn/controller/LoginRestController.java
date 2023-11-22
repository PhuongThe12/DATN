package luckystore.datn.controller;

import luckystore.datn.model.request.LoginRequest;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.service.LoginService;
import luckystore.datn.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @PostMapping("/login-basic")
    public ResponseEntity<?> loginBasic(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(loginService.loginBasic(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody TaiKhoanRequest request) {
        return new ResponseEntity<>(taiKhoanService.addTaiKhoan(request), HttpStatus.OK);
    }

}

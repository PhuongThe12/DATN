package luckystore.datn.controller;

import lombok.RequiredArgsConstructor;
import luckystore.datn.infrastructure.security.auth.JwtResponse;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.service.AuthenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenService authenService;

    @PostMapping("/singin")
    public ResponseEntity<JwtResponse> singin (@RequestBody TaiKhoanRequest requets)  {
        return ResponseEntity.ok(authenService.logInBasic(requets));
    }
}

package luckystore.datn.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckystore.datn.infrastructure.security.auth.JwtResponse;
import luckystore.datn.infrastructure.security.session.UserDetailToken;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.service.AuthenService;
import luckystore.datn.service.KhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenService authenService;

    private final KhachHangService khachHangService;

    @PostMapping("/singin")
    public ResponseEntity<?> singin (@Valid @RequestBody TaiKhoanRequest taiKhoanRequest, BindingResult result)  {

        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return ResponseEntity.ok(authenService.logInBasic(taiKhoanRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup (@Valid @RequestBody KhachHangRequest requets)  {
        return ResponseEntity.ok(khachHangService.dangKyKhachHang(requets));
    }

    private ResponseEntity<?> getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
                fieldErrors.add(errorMessage);
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity<>(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}

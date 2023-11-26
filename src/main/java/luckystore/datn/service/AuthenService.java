package luckystore.datn.service;

import luckystore.datn.infrastructure.security.auth.JwtResponse;
import luckystore.datn.infrastructure.security.auth.TokenRefreshRequest;
import luckystore.datn.model.request.TaiKhoanRequest;

public interface AuthenService {

    String signUp(TaiKhoanRequest taiKhoanRequest);

    JwtResponse logInBasic(TaiKhoanRequest taiKhoanRequest);

    JwtResponse refreshToken(TokenRefreshRequest request);
}

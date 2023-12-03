package luckystore.datn.service;

import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.infrastructure.security.auth.JwtResponse;
import luckystore.datn.infrastructure.security.auth.TokenRefreshRequest;
import luckystore.datn.infrastructure.security.session.UserDetailToken;
import luckystore.datn.model.request.TaiKhoanRequest;

public interface AuthenService {

    TaiKhoan signUp(TaiKhoanRequest taiKhoanRequest);

    JwtResponse logInBasic(TaiKhoanRequest taiKhoanRequest);

    JwtResponse refreshToken(TokenRefreshRequest request);

    UserDetailToken getUserByToken(String token);
}

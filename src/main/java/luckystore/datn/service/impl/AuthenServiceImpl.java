package luckystore.datn.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.RestApiException;
import luckystore.datn.infrastructure.security.auth.JwtResponse;
import luckystore.datn.infrastructure.security.auth.TokenRefreshRequest;
import luckystore.datn.infrastructure.security.session.UserDetailToken;
import luckystore.datn.infrastructure.security.token.TokenProvider;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.AuthenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenServiceImpl implements AuthenService {

    private final TaiKhoanRepository taiKhoanRepository;

    private final NhanVienRepository nhanVienRepository;

    private final KhachHangRepository khachHangRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider provider;


    @Override
    public JwtResponse logInBasic(TaiKhoanRequest taiKhoanRequest) {
        Optional<TaiKhoan> taiKhoanCheck = taiKhoanRepository.findByTenDangNhap(taiKhoanRequest.getTenDangNhap());
        if (!taiKhoanCheck.isPresent()) {
            throw new RestApiException("Không tìm thấy tên đăng nhập.");
        }
        if (!passwordEncoder.matches(taiKhoanRequest.getMatKhau(), taiKhoanCheck.get().getPassword())) {
            throw new RestApiException("Mật khẩu không đúng.");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(taiKhoanRequest.getTenDangNhap(),
                taiKhoanRequest.getMatKhau()));
        String token = provider.generateToken(taiKhoanCheck.get());
        String refreshToken = provider.genetateRefreshToken(new HashMap<>(), taiKhoanCheck.get());
        UserDetailToken userDetailToken = getUserByToken(token);
        return JwtResponse.builder()
                .id(userDetailToken.getId())
                .userName(userDetailToken.getTenDangNhap())
                .role(userDetailToken.getRole())
                .refreshToken(refreshToken)
                .token(token)
                .build();
    }

    @Override
    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String tenDangNhap = provider.extractUserName(request.getToken());
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap).orElseThrow();
        if (provider.isTokenValid(request.getToken(), taiKhoan)) {
            String jwt = provider.generateToken(taiKhoan);
            return JwtResponse.builder()
                    .refreshToken(request.getToken())
                    .token(jwt)
                    .build();
        }
        return null;
    }

    @Override
    public UserDetailToken getUserByToken(String token) {
        return provider.decodeTheToken(token);
    }
}

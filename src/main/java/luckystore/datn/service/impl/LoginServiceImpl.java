package luckystore.datn.service.impl;

import luckystore.datn.config.SecurityTokenProvider;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.RestApiException;
import luckystore.datn.model.request.LoginRequest;
import luckystore.datn.model.response.JwtResponse;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SecurityTokenProvider provider;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String loginBasic(LoginRequest request) {
        TaiKhoan taiKhoanFind = taiKhoanRepository.findByTenDangNhap(request.getTenDangNhap());
        if (taiKhoanFind == null) {
            throw new NotFoundException("Không tìm thấy tên đăng nhập!");
        }
        if (!passwordEncoder.matches(request.getMatKhau(), taiKhoanFind.getMatKhau())) {
            throw new RestApiException("Mật khẩu không đúng!");
        }

        return provider.generateToken(taiKhoanFind);
    }

    @Override
    public String logout(String token) {
        return null;
    }
}

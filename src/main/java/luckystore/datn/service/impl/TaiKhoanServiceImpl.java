package luckystore.datn.service.impl;

import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.model.response.NhanVienResponse;
import luckystore.datn.model.response.TaiKhoanResponse;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.TaiKhoanService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<NhanVienResponse> getAll() {
        return null;
    }

    @Override
    public Page<NhanVienResponse> getPage(int page) {
        return null;
    }

    @Override
    public NhanVienResponse addNhanVien(NhanVienRequest nhanVienRequest) {
        return null;
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
        return null;
    }

    @Override
    public NhanVienResponse findById(Long id) {
        return null;
    }

    @Override
    public TaiKhoanResponse addTaiKhoan(TaiKhoanRequest taiKhoanRequest) {
        TaiKhoan taiKhoan = getTaiKhoan(new TaiKhoan(), taiKhoanRequest);
        return new TaiKhoanResponse(taiKhoanRepository.save(taiKhoan));
    }

    @Override
    public String thayDoiMatKhau(Long id, String mkCu, String mkMoi) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
                .orElseThrow();
        if(!passwordEncoder.matches(mkCu, taiKhoan.getPassword())) {
            throw new InvalidIdException(new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Mật khẩu không đúng"))));
        }

        taiKhoan.setMatKhau(passwordEncoder.encode(mkMoi));
        taiKhoanRepository.save(taiKhoan);
        return "Thay đổi mật khẩu thành công";
    }

    private TaiKhoan getTaiKhoan(TaiKhoan taiKhoan, TaiKhoanRequest request) {
        taiKhoan.setTenDangNhap(request.getTenDangNhap());
        taiKhoan.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        taiKhoan.setRole(request.getRole());
        taiKhoan.setTrangThai(request.getTrangThai() == null || request.getTrangThai() == 0 ? 0 : 1);
        return taiKhoan;
    }

//    public TaiKhoanResponse login(TaiKhoanRequest taiKhoanRequest) {
//        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhapAndMatKhau(taiKhoanRequest.getTenDangNhap(), taiKhoanRequest.getMatKhau());
//        if (taiKhoan == null) {
//            throw new NotFoundException(ErrorMessage.NOT_FOUND);
//        }
//        if (taiKhoan.getRole() == 1) { // Nhaan Vien Login
//            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry(taiKhoan.getTenDangNhap(), SystemHistory.LOGIN, "" + SystemHistoryLogger.getDateNow()));
//        }
//            SystemHistory.nhanVien = nhanVienRepository.findNhanVienByIdTaiKhoan(taiKhoan.getId());
//        return new TaiKhoanResponse(taiKhoan);
//    }
}

package luckystore.datn.service.impl;

import jakarta.mail.MessagingException;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.infrastructure.constants.Role;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.model.response.TaiKhoanResponse;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.TaiKhoanKhachHangService;
import luckystore.datn.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaiKhoanKhachHangServiceImpl implements TaiKhoanKhachHangService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private HangKhachHangRepository hangKhachHangRepo;


    @Override
    public Page<TaiKhoanResponse> getPage(int page, String searchText, Integer status) {
        return null;
    }

    @Override
    public TaiKhoanResponse addTaiKhoan(TaiKhoanRequest taiKhoanRequest) {
        try {
            emailUtil.sendOtpEmail(taiKhoanRequest.getTenDangNhap());
        } catch (MessagingException e) {
            throw new RuntimeException("không thể gửi mail");
        }

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(taiKhoanRequest.getTenDangNhap());
        taiKhoan.setMatKhau(taiKhoanRequest.getMatKhau());
        taiKhoan.setRole(Role.ROLE_ADMIN);
        taiKhoan.setTrangThai(1);
        taiKhoanRepository.save(taiKhoan);


        KhachHang khachHang = new KhachHang();
        khachHang.setEmail(taiKhoanRequest.getTenDangNhap());
        khachHang.setTrangThai(1);
        khachHang.setTaiKhoan(taiKhoan);
        khachHang.setDiemTichLuy(0);

        List<HangKhachHang> hangs = hangKhachHangRepo.getMaxByDiemTichLuy(khachHang.getDiemTichLuy(), PageRequest.of(0, 1));
        HangKhachHang hang = !hangs.isEmpty() ? hangs.get(0) : null;
        khachHang.setHangKhachHang(hang);

        khachHangRepository.save(khachHang);
        return new TaiKhoanResponse(taiKhoan);
    }

//    @Override
//    public TaiKhoanResponse khachHanglogin(TaiKhoanRequest taiKhoanRequest) {
//        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhapAndMatKhau(taiKhoanRequest.getTenDangNhap(), taiKhoanRequest.getMatKhau());
//        if (taiKhoan != null && taiKhoan.getRole() == 2) {
//            return new TaiKhoanResponse(taiKhoan);
//        }else{
//            throw new NotFoundException(ErrorMessage.NOT_FOUND);
//        }
//
//    }

}

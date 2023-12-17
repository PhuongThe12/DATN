package luckystore.datn.service.impl;

import luckystore.datn.entity.GioHang;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.infrastructure.constants.Role;
import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.KhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KhachHangServiceImpl implements KhachHangService {


    @Autowired
    private KhachHangRepository khachHangRepo;

    @Autowired
    private HangKhachHangRepository hangKhachHangRepo;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GioHangRepository gioHangRepository;

    @Override
    public List<KhachHangResponse> getAll() {
        List<KhachHangResponse> list = khachHangRepo.findAllResponse();
        return khachHangRepo.findAllResponse();
    }

    @Override
    public Page<KhachHangResponse> getPage(int page, String searchText, Integer status) {
        return khachHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    @Transactional
    public KhachHangResponse addKhachHang(KhachHangRequest khachHangRequest) {

        if (khachHangRepo.existsByEmail(khachHangRequest.getEmail())) {
            String errorObject = JsonString.errorToJsonObject("email", "Email đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }

        KhachHang khachHang = getKhachHang(new KhachHang(), khachHangRequest);
        khachHang.setDiemTichLuy(khachHangRequest.getDiemTichLuy());

        setHangKhachHang(khachHang);

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMatKhau(khachHangRequest.getSoDienThoai());
        taiKhoan.setTenDangNhap(khachHangRequest.getEmail());
        taiKhoan.setRole(Role.ROLE_USER);
        taiKhoan.setTrangThai(khachHang.getTrangThai());

        setHangKhachHang(khachHang);

        taiKhoan.setMatKhau(passwordEncoder.encode(khachHangRequest.getSoDienThoai()));
        taiKhoan.setTenDangNhap(khachHangRequest.getEmail());
        taiKhoan.setRole(Role.ROLE_USER);
        taiKhoan.setTrangThai(1);
        taiKhoanRepository.save(taiKhoan);

        khachHang.setTaiKhoan(taiKhoan);
        khachHang = khachHangRepo.save(khachHang);
        GioHang gioHang = new GioHang();
        gioHang.setGhiChu("Giỏ hàng ");
        gioHang.setTrangThai(1);
        gioHang.setNgayTao(LocalDateTime.now());
        gioHang.setKhachHang(khachHang);
        gioHangRepository.save(gioHang);

        return new KhachHangResponse(khachHang);
    }

    private void setHangKhachHang(KhachHang khachHang) {
        if (khachHang.getDiemTichLuy() != null) {
            List<HangKhachHang> hangs = hangKhachHangRepo.getMaxByDiemTichLuy(khachHang.getDiemTichLuy(), PageRequest.of(0, 1));
            HangKhachHang hang = !hangs.isEmpty() ? hangs.get(0) : null;
            khachHang.setHangKhachHang(hang);
        } else {
            khachHang.setHangKhachHang(null);
        }
    }

    @Override
    public KhachHangResponse updateKhachHang(Long id, KhachHangRequest khachHangRequest) {

        KhachHang khachHang;
        if (id == null) {
            throw new NullException();
        } else {
            khachHang = khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        if (khachHangRepo.existsByEmailAndIdNot(khachHangRequest.getEmail(), khachHang.getId())) {
            String errorObject = JsonString.errorToJsonObject("email", "Email khách hàng đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }

        getKhachHang(khachHang, khachHangRequest);
        setHangKhachHang(khachHang);
        khachHang.getTaiKhoan().setTrangThai(khachHang.getTrangThai());
        return new KhachHangResponse(khachHangRepo.save(khachHang));
    }

    @Override
    public KhachHangResponse findById(Long id) {
        return new KhachHangResponse(khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<KhachHangResponse> searchByName(String searchText) {
        return khachHangRepo.searchByName(searchText);
    }

    @Override
    @Transactional
    public KhachHangResponse dangKyKhachHang(KhachHangRequest request) {

        KhachHang khachHang = khachHangRepo.getByEmail(request.getEmail());

        GioHang gioHang = null;
        if (khachHang == null) {
            khachHang = getKhachHang(new KhachHang(), request);
            gioHang = new GioHang();
            gioHang.setGhiChu("Giỏ hàng ");
            gioHang.setTrangThai(1);
            gioHang.setNgayTao(LocalDateTime.now());
        }else {
            khachHang = getKhachHang(khachHang, request);
            if(khachHang.getTaiKhoan() != null) {
                if(khachHang.getTaiKhoan().getTrangThai() == 1) {
                    String errorObject = JsonString.errorToJsonObject("email", "Tài khoản đã được đăng ký. Nếu là tài khooản của bạn hãy ấn vào quên mật khẩu");
                    throw new DuplicateException(JsonString.stringToJson(errorObject));
                } else {
                    TaiKhoan taiKhoan = khachHang.getTaiKhoan();
                    taiKhoan.setTrangThai(0);
                    taiKhoan.setMatKhau(passwordEncoder.encode(request.getPassword()));
                    khachHang.setTaiKhoan(taiKhoan);
                    return new KhachHangResponse(khachHangRepo.save(khachHang));
                }
            }
        }


        khachHang.setDiemTichLuy(0);
        setHangKhachHang(khachHang);

        TaiKhoan taiKhoan = new TaiKhoan();

        setHangKhachHang(khachHang);

        taiKhoan.setMatKhau(passwordEncoder.encode(request.getPassword()));
        taiKhoan.setTenDangNhap(request.getEmail());
        taiKhoan.setRole(Role.ROLE_USER);
        taiKhoan.setTrangThai(0);
        khachHang.setTaiKhoan(taiKhoan);

        khachHang = khachHangRepo.save(khachHang);
        if(gioHang != null) {
            gioHang.setKhachHang(khachHang);
            gioHangRepository.save(gioHang);
        }

        return new KhachHangResponse(khachHang);
    }

    @Override
    public KhachHangResponse updateMotPhanKhachHang(Long id, KhachHangRequest khachHangRequest) {
        KhachHang khachHang = khachHangRepo.findById(id).orElseThrow();
        khachHang.setHoTen(khachHangRequest.getHoTen());
        khachHang.setSoDienThoai(khachHangRequest.getSoDienThoai());
        khachHang.setGioiTinh(khachHangRequest.getGioiTinh());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        return new KhachHangResponse(khachHangRepo.save(khachHang));
    }

    private KhachHang getKhachHang(KhachHang khachHang, KhachHangRequest khachHangRequest) {
        khachHang.setHoTen(khachHangRequest.getHoTen());
        khachHang.setGioiTinh(khachHangRequest.getGioiTinh());
        khachHang.setSoDienThoai(khachHangRequest.getSoDienThoai());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        khachHang.setEmail(khachHangRequest.getEmail());
        khachHang.setDiemTichLuy(khachHangRequest.getDiemTichLuy());
        khachHang.setTrangThai(khachHangRequest.getTrangThai() == null || khachHangRequest.getTrangThai() == 0 ? 0 : 1);
        return khachHang;
    }

}




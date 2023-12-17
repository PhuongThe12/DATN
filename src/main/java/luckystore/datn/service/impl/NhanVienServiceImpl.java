package luckystore.datn.service.impl;

import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.infrastructure.constraints.SystemHistory;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.SystemHistoryEntry;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.infrastructure.constants.Role;
import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.model.response.NhanVienResponse;
import luckystore.datn.repository.NhanVienRepository;
import luckystore.datn.repository.TaiKhoanRepository;
import luckystore.datn.service.NhanVienService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<NhanVienResponse> getAll() {
        System.out.println(nhanVienRepo.findAllResponse());
        return nhanVienRepo.findAllResponse();
    }

    @Override
    public Page<NhanVienResponse> getPage(int page, String searchText, Integer status, Integer chucVu) {
        return nhanVienRepo.getPageResponse(searchText, status, chucVu, PageRequest.of((page - 1), 5));
    }

    @Override
    public NhanVienResponse addNhanVien(NhanVienRequest nhanVienRequest) {
        checkWhenInsertTaiKhoan(nhanVienRequest);
        checkWhenInsert(nhanVienRequest);
 
        NhanVien nhanVien = new NhanVien();

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(nhanVienRequest.getEmail());
        taiKhoan.setMatKhau(passwordEncoder.encode(nhanVienRequest.getMatKhau()));
        taiKhoan.setTrangThai(1);
        getNhanVien(nhanVien, nhanVienRequest);

        taiKhoan.setRole(nhanVien.getChucVu() == 2? Role.ROLE_ADMIN : Role.ROLE_STAFF);

        taiKhoan = taiKhoanRepo.save(taiKhoan);
        nhanVien.setTaiKhoan(taiKhoan);

        return new NhanVienResponse(nhanVienRepo.save(nhanVien));
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
        checkWhenUpdateSoDienThoai(id, nhanVienRequest);
        NhanVien nhanVien;
        if (id == null) {
            throw new NullException();
        } else {
            nhanVien = nhanVienRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        nhanVien.setHoTen(nhanVienRequest.getHoTen());
        nhanVien.setGioiTinh(nhanVienRequest.getGioiTinh());
        nhanVien.setNgaySinh(nhanVienRequest.getNgaySinh());
        nhanVien.setSoDienThoai(nhanVienRequest.getSoDienThoai());
        nhanVien.setGhiChu(nhanVienRequest.getGhiChu());
        nhanVien.setTrangThai(nhanVienRequest.getTrangThai());
        nhanVien.setXa(nhanVienRequest.getXa());
        nhanVien.setHuyen(nhanVienRequest.getHuyen());
        nhanVien.setTinh(nhanVienRequest.getTinh());
        nhanVien.setChucVu(nhanVienRequest.getChucVu());

        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        taiKhoan.setTrangThai(nhanVienRequest.getTrangThai());

        return new NhanVienResponse(nhanVienRepo.save(nhanVien));
    }

    @Override
    public NhanVienResponse findById(Long id) {
        return new NhanVienResponse(nhanVienRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public NhanVienResponse findNhanVienByIdTaiKhoan(Long id) {
        NhanVien nhanVien = nhanVienRepo.findNhanVienByIdTaiKhoan(id);
        if (nhanVienRepo.findNhanVienByIdTaiKhoan(id) == null) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
        return new NhanVienResponse(nhanVien);
    }

    @Override
    public List<SystemHistoryEntry> getLogger() {
        return SystemHistoryLogger.readSystemHistoryEntries();
    }

    @Override
    public void resetPassword(Long id) {
        NhanVien nhanVien = nhanVienRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        taiKhoan.setMatKhau(passwordEncoder.encode("123456"));
        taiKhoanRepo.save(taiKhoan);
    }

    private NhanVien getNhanVien(NhanVien nhanVien, NhanVienRequest nhanVienRequest) {
        nhanVien.setHoTen(nhanVienRequest.getHoTen());
        nhanVien.setGioiTinh(nhanVienRequest.getGioiTinh());
        nhanVien.setNgaySinh(nhanVienRequest.getNgaySinh());
        nhanVien.setSoDienThoai(nhanVienRequest.getSoDienThoai());
        nhanVien.setEmail(nhanVienRequest.getEmail());
        nhanVien.setGhiChu(nhanVienRequest.getGhiChu());
        nhanVien.setTrangThai(nhanVienRequest.getTrangThai());
        nhanVien.setXa(nhanVienRequest.getXa());
        nhanVien.setHuyen(nhanVienRequest.getHuyen());
        nhanVien.setTinh(nhanVienRequest.getTinh());
        nhanVien.setChucVu(nhanVienRequest.getChucVu());

        return nhanVien;
    }

    private void checkWhenInsert(NhanVienRequest nhanVienRequest) {
        if (nhanVienRepo.existsBySoDienThoai(nhanVienRequest.getSoDienThoai())) {
            String errorObject = JsonString.errorToJsonObject("data", "Số điện thoại đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        } else if (nhanVienRepo.existsByEmail(nhanVienRequest.getEmail())) {
            String errorObject = JsonString.errorToJsonObject("data", "Email đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenInsertTaiKhoan(NhanVienRequest nhanVienRequest) {
        if (taiKhoanRepo.existsByTenDangNhap(nhanVienRequest.getEmail())) {
            String errorObject = JsonString.errorToJsonObject("data", "Email đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdateSoDienThoai(Long id, NhanVienRequest nhanVienRequest) {
        String errorObject = "";
        if (nhanVienRepo.existsBySoDienThoaiAndIdNot(id, nhanVienRequest.getSoDienThoai())) {
            errorObject = JsonString.errorToJsonObject("data", "Số điện thoại đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

}

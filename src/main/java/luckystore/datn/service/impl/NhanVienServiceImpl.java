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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

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
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(nhanVienRequest.getTenDangNhap());
        taiKhoan.setMatKhau(nhanVienRequest.getMatKhau());
        taiKhoan.setTrangThai(1);
        taiKhoan.setRole(Role.ROLE_STAFF);
        NhanVien nhanVien = getNhanVien(new NhanVien(), nhanVienRequest);
        nhanVien.setTaiKhoan(taiKhoanRepo.save(taiKhoan));
        if (SystemHistory.nhanVien == null) {
            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry("Guest", SystemHistory.ADD_NV, "" + SystemHistoryLogger.getDateNow()));
        } else {
            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry(SystemHistory.nhanVien.getTaiKhoan().getTenDangNhap(), SystemHistory.ADD_NV, "" + SystemHistoryLogger.getDateNow()));
        }
        return new NhanVienResponse(nhanVienRepo.save(nhanVien));
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
        checkWhenUpdateSoDienThoai(nhanVienRequest);
        checkWhenUpdateEmail(nhanVienRequest);
        NhanVien nhanVien;
        if (id == null) {
            throw new NullException();
        } else {
            nhanVien = nhanVienRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setId(nhanVienRequest.getIdTaiKhoan());
        taiKhoan.setTenDangNhap(nhanVienRequest.getTenDangNhap());
        taiKhoan.setMatKhau(nhanVienRequest.getMatKhau());
        taiKhoan.setTrangThai(1);
        taiKhoan.setRole(nhanVienRequest.getRole());
        nhanVien.setTaiKhoan(taiKhoanRepo.save(taiKhoan));
        nhanVien = getNhanVien(nhanVien, nhanVienRequest);
        if (SystemHistory.nhanVien == null) {
            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry("Guest", SystemHistory.UPDATE_NV, "" + SystemHistoryLogger.getDateNow()));
        } else if (nhanVienRequest.getUpdateAccount() != null) {
            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry(SystemHistory.nhanVien.getTaiKhoan().getTenDangNhap(), SystemHistory.UPDATE_ACCOUNT_INFO, "" + SystemHistoryLogger.getDateNow()));
        } else {
            SystemHistoryLogger.writeSystemHistoryEntry(new SystemHistoryEntry(SystemHistory.nhanVien.getTaiKhoan().getTenDangNhap(), SystemHistory.UPDATE_NV, "" + SystemHistoryLogger.getDateNow()));

        }

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
            String errorObject = JsonString.errorToJsonObject("soDienThoai", "Số điện thoại đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        } else if (nhanVienRepo.existsByEmail(nhanVienRequest.getEmail())) {
            String errorObject = JsonString.errorToJsonObject("email", "Email đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenInsertTaiKhoan(NhanVienRequest nhanVienRequest) {
        if (taiKhoanRepo.existsByTenDangNhap(nhanVienRequest.getTenDangNhap())) {
            String errorObject = JsonString.errorToJsonObject("tenDangNhap", "Tài Khoản đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdateSoDienThoai(NhanVienRequest nhanVienRequest) {
        NhanVien nhanVien = nhanVienRepo.findNhanVienBySoDienThoai(nhanVienRequest.getSoDienThoai());
        String errorObject = "";
        if (nhanVien != null && !Objects.equals(nhanVien.getId(), nhanVienRequest.getId())) {
            errorObject = JsonString.errorToJsonObject("soDienThoai", "Số điện thoại đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdateEmail(NhanVienRequest nhanVienRequest) {
        NhanVien nhanVien = nhanVienRepo.findNhanVienByEmail(nhanVienRequest.getEmail());
        String errorObject = "";
        if (nhanVien != null && !Objects.equals(nhanVien.getId(), nhanVienRequest.getId())) {
            errorObject = JsonString.errorToJsonObject("email", "Email đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

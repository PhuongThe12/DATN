package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
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
        NhanVien nhanVien = getNhanVien(new NhanVien(), nhanVienRequest);
        nhanVien.setTaiKhoan(taiKhoanRepo.save(taiKhoan));
        return new NhanVienResponse(nhanVienRepo.save(nhanVien));
    }

    @Override
    public NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest) {
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
        nhanVien.setTaiKhoan(taiKhoanRepo.save(taiKhoan));
        nhanVien = getNhanVien(nhanVien, nhanVienRequest);
        return new NhanVienResponse(nhanVienRepo.save(nhanVien));
    }

    @Override
    public NhanVienResponse findById(Long id) {
        return new NhanVienResponse(nhanVienRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
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
        if (nhanVienRepo.existsByHoTen(nhanVienRequest.getHoTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenInsertTaiKhoan(NhanVienRequest nhanVienRequest) {
        if (taiKhoanRepo.existsByTenDangNhap(nhanVienRequest.getTenDangNhap())) {
            String errorObject = JsonString.errorToJsonObject("tenDangNhap", "Tài Khoản đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(NhanVienRequest nhanVienRequest) {
        if (nhanVienRepo.existsByHoTenAndIdNot(nhanVienRequest.getHoTen(), nhanVienRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

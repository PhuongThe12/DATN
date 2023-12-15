package luckystore.datn.service;

import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.model.response.NhanVienResponse;
import luckystore.datn.model.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaiKhoanService {

    List<NhanVienResponse> getAll();

    Page<NhanVienResponse> getPage(int page);

    NhanVienResponse addNhanVien(NhanVienRequest nhanVienRequest);

    NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest);

    NhanVienResponse findById(Long id);

    TaiKhoanResponse addTaiKhoan(TaiKhoanRequest taiKhoanRequest);

    String thayDoiMatKhau(Long id, String mkCu, String mkMoi);
}

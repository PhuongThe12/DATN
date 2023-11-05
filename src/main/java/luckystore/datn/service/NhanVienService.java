package luckystore.datn.service;

import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.model.response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NhanVienService {
    List<NhanVienResponse> getAll();

    Page<NhanVienResponse> getPage(int page, String searchText, Integer status, Integer chucVu);

    NhanVienResponse addNhanVien(NhanVienRequest nhanVienRequest);

    NhanVienResponse updateNhanVien(Long id, NhanVienRequest nhanVienRequest);

    NhanVienResponse findById(Long id);

    NhanVienResponse findNhanVienByIdTaiKhoan(Long id);
}

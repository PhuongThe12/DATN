package luckystore.datn.service;

import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SanPhamYeuThichService {

    Page<SanPhamYeuThichResponse> getPage(int page, String searchText, Long idKhachHang);

    SanPhamYeuThichResponse addSanPhamYeuThich(SanPhamYeuThichRequest sanPhamYeuThichRequest);

    void deleteSanPhamYeuThichByIdKhachHangAndIdGiay(Long idKhachHang, Long idGiay);

    void deleteSanPhamYeuThick(Long id);

    boolean existsByIdKhachHangAndIdIdGiay(Long idKhachHang, Long idGiay);
}

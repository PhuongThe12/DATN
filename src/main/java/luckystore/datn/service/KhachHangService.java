package luckystore.datn.service;

import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.KhachHangResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KhachHangService {
    List<KhachHangResponse> getAll();

    Page<KhachHangResponse> getPage(int page, String searchText, Integer status);

    KhachHangResponse addKhachHang(KhachHangRequest khachHangRequest);

    KhachHangResponse updateKhachHang(Long id, KhachHangRequest khachHangRequest);

    KhachHangResponse findById(Long id);

    List<KhachHangResponse> searchByName(String searchText);

    KhachHangResponse dangKyKhachHang(KhachHangRequest requets);
}

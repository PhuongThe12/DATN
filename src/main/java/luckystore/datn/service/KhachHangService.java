package luckystore.datn.service;

import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.KhachHangRestponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KhachHangService {
    List<KhachHangRestponse> getAll();

    Page<KhachHangRestponse> getPage(int page, String searchText, Integer status);

    KhachHangRestponse addKhachHang(KhachHangRequest khachHangRequest);

    KhachHangRestponse updateKhachHang(Long id, KhachHangRequest khachHangRequest);

    KhachHangRestponse findById(Long id);
}

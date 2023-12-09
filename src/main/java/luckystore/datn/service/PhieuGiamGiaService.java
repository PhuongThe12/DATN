package luckystore.datn.service;

import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.model.request.FindPhieuGiamGiaRequest;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PhieuGiamGiaService {

    List<PhieuGiamGiaResponse> getAll();

    Page<PhieuGiamGiaResponse> getpage(int page, String searchText, Integer status);

    Page<PhieuGiamGiaResponse> getListSearchPhieu(FindPhieuGiamGiaRequest request);

    PhieuGiamGiaResponse getPhieuResponseById(Long id);

    PhieuGiamGia addPhieuGiamGia(PhieuGiamGiaRequest request);

    PhieuGiamGia updatePhieuGiamGia(Long id, PhieuGiamGiaRequest request);

    List<PhieuGiamGiaResponse>getListPhieuByHangKhachHang(String hangKhachHang);
}

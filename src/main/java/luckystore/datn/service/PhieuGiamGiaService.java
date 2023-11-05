package luckystore.datn.service;

import luckystore.datn.common.PageableRequest;
import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PhieuGiamGiaService {

    List<PhieuGiamGiaResponse> getAll();

    Page<PhieuGiamGiaResponse> getPagePhieuGiamGia(PageableRequest request);

    PhieuGiamGiaResponse findPhieuGiamGiaById(Long id);

    PhieuGiamGia addPhieuGiamGia(PhieuGiamGiaRequest request);

    PhieuGiamGia updatePhieuGiamGia(Long id, PhieuGiamGiaRequest request);
}

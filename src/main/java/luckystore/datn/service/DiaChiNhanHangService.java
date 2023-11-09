package luckystore.datn.service;

import luckystore.datn.model.request.DiaChiNhanHangRequest;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import luckystore.datn.model.response.KhachHangResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiaChiNhanHangService {

    Page<DiaChiNhanHangResponse> getPage(int page, String searchText, Integer status);

    DiaChiNhanHangResponse addDiaChiNhanHang(DiaChiNhanHangRequest diaChiNhanHangRequest);

    DiaChiNhanHangResponse updateDiaChiNhanHang(Long id, DiaChiNhanHangRequest diaChiNhanHangRequest);

    DiaChiNhanHangResponse findById(Long id);
}

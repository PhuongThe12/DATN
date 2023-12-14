package luckystore.datn.service;

import luckystore.datn.model.request.DanhGiaRequest;
import luckystore.datn.model.request.DotGiamGiaRequest;
import luckystore.datn.model.response.DanhGiaResponse;
import luckystore.datn.model.response.DotGiamGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface DanhGiaService {

    Page<DanhGiaResponse> getPage(int page, Integer star);

    void deleteDanhGia(Long id);

    DanhGiaResponse addDanhGia(DanhGiaRequest danhGiaRequest);


}

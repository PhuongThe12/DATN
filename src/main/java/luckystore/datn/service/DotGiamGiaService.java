package luckystore.datn.service;

import luckystore.datn.model.request.DotGiamGiaRequest;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.DotGiamGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DotGiamGiaService {

    List<DotGiamGiaResponse> getAll();

    Page<DotGiamGiaResponse> getPage(int page, String searchText, Integer status);

    DotGiamGiaResponse addDotGiamGia(DotGiamGiaRequest dotGiamGiaRequest);

    DotGiamGiaResponse updateDotGiamGia(Long id, DotGiamGiaRequest dotGiamGiaRequest);

    DotGiamGiaResponse findById(Long id);

    List<DotGiamGiaResponse> getAllActive();

    Page<DotGiamGiaResponse> searchingDotGiamGia(KhuyenMaiSearch kmSearch);

    Long hienThiDotGiamGia(Long id);

    Long anDotGiamGia(Long id);
}

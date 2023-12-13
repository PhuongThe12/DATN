package luckystore.datn.service;

import luckystore.datn.model.request.DotGiamGiaRequest;
import luckystore.datn.model.request.KhuyenMaiRequest;
import luckystore.datn.model.response.DotGiamGiaResponse;
import luckystore.datn.model.response.KhuyenMaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface KhuyenMaiService {

    List<KhuyenMaiResponse> getAll();

    Page<KhuyenMaiResponse> getPage(int page, String searchText, Integer status);

    KhuyenMaiResponse addKhuyenMai(KhuyenMaiRequest khuyenMaiRequest);
    KhuyenMaiResponse updateKhuyenMai(Long id, KhuyenMaiRequest khuyenMaiRequest);

    KhuyenMaiResponse findById(Long id);

}

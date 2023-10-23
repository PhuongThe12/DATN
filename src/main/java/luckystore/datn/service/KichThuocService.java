package luckystore.datn.service;

import luckystore.datn.model.request.KichThuocRequest;
import luckystore.datn.model.response.KichThuocResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KichThuocService {
    List<KichThuocResponse> getAll();

    Page<KichThuocResponse> getPage(int page, String searchText, Integer status);

    KichThuocResponse addKichThuoc(KichThuocRequest kichThuocRequest);

    KichThuocResponse updateKichThuoc(Long id, KichThuocRequest kichThuocRequest);

    KichThuocResponse findById(Long id);
}

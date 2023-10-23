package luckystore.datn.service;

import luckystore.datn.model.request.ThuongHieuRequest;
import luckystore.datn.model.response.ThuongHieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThuongHieuService {
    List<ThuongHieuResponse> getAll();

    Page<ThuongHieuResponse> getPage(int page, String searchText, Integer status);

    ThuongHieuResponse addThuongHieu(ThuongHieuRequest thuongHieuRequest);

    ThuongHieuResponse updateThuongHieu(Long id, ThuongHieuRequest thuongHieuRequest);

    ThuongHieuResponse findById(Long id);
}


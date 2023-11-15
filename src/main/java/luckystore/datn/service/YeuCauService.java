package luckystore.datn.service;

import luckystore.datn.entity.YeuCau;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface YeuCauService {

    List<YeuCauResponse> getAll();

    YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest);

    YeuCauResponse updateYeuCau(Long id, YeuCauRequest yeuCauRequest);

    YeuCauResponse findById(Long id);
    YeuCauResponse findByStatus();
    Page<YeuCauResponse> getPage(Integer page,String searchText,Date ngayBatDau, Date ngayKetThuc, Integer loaiYeuCau, Integer trangThai);
}

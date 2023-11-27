package luckystore.datn.service;

import luckystore.datn.entity.YeuCau;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public interface YeuCauService {

    List<YeuCauResponse> getAll();

    YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest);

    YeuCauResponse confirmYeuCau(YeuCauRequest yeuCauRequest);
    YeuCauResponse unConfirmYeuCau(YeuCauRequest yeuCauRequest);
    YeuCauResponse updateYeuCau(YeuCauRequest yeuCauRequest);

    YeuCauResponse findById(Long id);

    YeuCauResponse findByStatus();
    Page<YeuCauResponse> getPage(Integer page, Long searchText, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai);

}

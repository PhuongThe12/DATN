package luckystore.datn.service;

import luckystore.datn.entity.YeuCau;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface YeuCauChiTietService {

    YeuCauChiTietResponse addYeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest, YeuCau yeuCau);

    List<YeuCauChiTietResponse> getAllYeuCauChiTietResponse(Long id);

}

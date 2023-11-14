package luckystore.datn.service;

import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import org.springframework.stereotype.Service;

@Service
public interface YeuCauChiTietService {

    YeuCauChiTietResponse addYeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest);
}

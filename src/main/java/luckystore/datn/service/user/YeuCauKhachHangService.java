package luckystore.datn.service.user;

import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.YeuCauResponse;


public interface YeuCauKhachHangService {
    HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id);

    YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest);
}

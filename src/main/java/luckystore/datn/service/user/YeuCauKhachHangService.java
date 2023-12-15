package luckystore.datn.service.user;

import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface YeuCauKhachHangService {
    HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id);

    YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest);
    List<YeuCauResponse> getListYeuCau(Long idHoaDon);
}

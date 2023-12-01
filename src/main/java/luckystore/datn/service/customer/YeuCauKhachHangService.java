package luckystore.datn.service.customer;

import luckystore.datn.model.response.HoaDonYeuCauRespone;
import org.springframework.stereotype.Service;


public interface YeuCauKhachHangService {
    HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id);
}

package luckystore.datn.service;

import luckystore.datn.model.response.GioHangResponse;
import org.springframework.stereotype.Service;

@Service
public interface GioHangService {
    public GioHangResponse getGioHangByKhachHang(Long id);

}

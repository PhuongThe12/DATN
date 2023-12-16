package luckystore.datn.service.user;

import jakarta.mail.MessagingException;
import luckystore.datn.model.request.GioHangThanhToanRequest;
import luckystore.datn.model.request.HoaDonChiTietRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.response.HoaDonResponse;
import org.springframework.stereotype.Service;

@Service
public interface HoaDonKhachHangService {

    HoaDonResponse addHoaDon(GioHangThanhToanRequest gioHangThanhToanRequest) throws MessagingException;

    void cancelBankingOrder(Long id);
    HoaDonResponse findById(Long id);
    Long hoanTatThanhToan(HoaDonThanhToanTaiQuayRequest request);
}

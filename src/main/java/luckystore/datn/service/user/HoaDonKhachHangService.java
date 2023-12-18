package luckystore.datn.service.user;

import jakarta.mail.MessagingException;
import luckystore.datn.model.request.*;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
import org.springframework.stereotype.Service;

@Service
public interface HoaDonKhachHangService {

    HoaDonResponse addHoaDon(GioHangThanhToanRequest gioHangThanhToanRequest) throws MessagingException;

    void cancelBankingOrder(Long id);
    HoaDonResponse findById(Long id);
    Long hoanTatThanhToan(HoaDonThanhToanTaiQuayRequest request);

    HoaDonResponse sendMailHoaDon(Long idHoaDon) throws MessagingException;

    HoaDonResponse capNhatDiaChiNhan(HoaDonDiaChiNhanRequest hoaDonDiaChiNhanRequest);

    HoaDonPrintResponse getThanhToanChiTiet(Long idHoaDon);

    HoaDonResponse huyDonHang(HuyDonRequest huyDonRequest);
}

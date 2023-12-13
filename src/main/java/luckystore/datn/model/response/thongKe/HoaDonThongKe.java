package luckystore.datn.model.response.thongKe;

import lombok.val;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.KhachHang;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = {HoaDonChiTiet.class, HoaDon.class, KhachHang.class, ChiTietThanhToan.class})
public interface HoaDonThongKe {

    @Value("#{target.HO_TEN}")
    String getHoTen();

    @Value("#{target.NGAY_TAO}")
    String getNgayTao();

    @Value("#{target.TRANG_THAI}")
    String getTrangThai();

    @Value("#{target.TIEN_THANH_TOAN}")
    String getTienThanhToan();

}

package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

public interface SanPhamBanChay {

    @Value("#{target.tenKhachHang}")
    String getTenKhachHang();

    @Value("#{target.giaSanPham}")
    String getGiaSanPham();

    @Value("#{target.soLuongBan}")
    String getSoLuongBan();
}

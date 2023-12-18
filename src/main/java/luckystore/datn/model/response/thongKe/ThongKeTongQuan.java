package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ThongKeTongQuan {

    @Value("#{target.tongDoanhThu}")
    BigDecimal getTongDoanhThu();

    @Value("#{target.tongHoaDon}")
    Integer getTongHoaDon();

    @Value("#{target.tongSanPham}")
    Integer getTongSanPham();

}

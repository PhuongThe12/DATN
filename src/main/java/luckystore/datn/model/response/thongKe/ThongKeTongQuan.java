package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ThongKeTongQuan {

    @Value("#{target.tongHoaDon}")
    Integer getTongHoaDon();
    @Value("#{target.tongTien}")
    BigDecimal getTongTien();

    @Value("#{target.tongSanPham}")
    Integer getTongSanPham();

    @Value("#{target.tongYeuCau}")
    Integer getTongYeuCau();
}

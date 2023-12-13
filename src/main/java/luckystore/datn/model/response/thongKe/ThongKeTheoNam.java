package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ThongKeTheoNam {

    @Value("#{target.thang}")
    Integer getThang();

    @Value("#{target.tongSanPham}")
    Integer getTongSanPham();
    @Value("#{target.tongTien}")
    BigDecimal getTongTien();
}

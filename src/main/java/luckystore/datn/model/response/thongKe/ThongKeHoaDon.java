package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ThongKeHoaDon {

    @Value("#{target.tongHoaDon}")
    Integer getTongHoaDon();

    @Value("#{target.tongSanPham}")
    Integer getTongSanPham();

    @Value("#{target.tongYeuCau}")
    Integer getTongYeuCau();
}

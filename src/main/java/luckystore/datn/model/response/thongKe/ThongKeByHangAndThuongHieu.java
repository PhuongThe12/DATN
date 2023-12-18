package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface ThongKeByHangAndThuongHieu {

    @Value("#{target.ten}")
    String getTen();

    @Value("#{target.tongDoanhThu}")
    BigDecimal getTongDoanhThu();
}

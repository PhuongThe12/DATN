package luckystore.datn.model.response.thongKe;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;

public interface ThongKeHoaDon {

    @Value("#{target.ngay}")
    Date getNgay();

    @Value("#{target.soLuongHoaDon}")
    Integer getSoLuongHoaDon();

    @Value("#{target.soLuongSanPham}")
    Integer getSoLuongSanPham();

    @Value("#{target.tongTien}")
    Integer getTongTien();

    @Value("#{target.HO_TEN}")
    String getHoTen();

}

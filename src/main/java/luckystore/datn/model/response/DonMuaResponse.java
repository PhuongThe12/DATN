package luckystore.datn.model.response;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.ThuongHieu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(types = {HoaDonChiTiet.class, HoaDon.class, BienTheGiay.class, Giay.class, ThuongHieu.class})
public interface DonMuaResponse {

    @Value("#{target.ID_HOA_DON}")
    String getIdHoaDon();

    @Value("#{target.HINH_ANH}")
    String getHinhAnh();

    @Value("#{target.TEN_GIAY}")
    String getTenGiay();

    @Value("#{target.TEN_THUONG_HIEU}")
    String getThuongHieu();

    @Value("#{target.ID_BIEN_THE_GIAY}")
    Long getIdBienTheGiay();

    @Value("#{target.ID_KHACH_HANG}")
    Long getIdKhachHang();

    @Value("#{target.SO_LUONG}")
    Integer getSoLuong();

    @Value("#{target.DON_GIA}")
    BigDecimal getDonGia();

    @Value("#{target.TONG_TIEN}")
    BigDecimal getTongTien();

    @Value("#{target.PHI_SHIP}")
    BigDecimal phiShip();

    @Value("#{target.TRANG_THAI}")
    BigDecimal getTrangThai();

}

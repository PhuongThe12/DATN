package luckystore.datn.model.response;

import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.PhieuGiamGia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(types = {PhieuGiamGia.class, HangKhachHang.class, Giay.class} )
public interface PhieuGiamGiaResponse {

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.MA_GIAM_GIA}")
    String getMaGiamGia();

    @Value("#{target.PHAN_TRAM_GIAM}")
    Integer getPhanTramGiam();

    @Value("#{target.SO_LUONG_PHIEU}")
    Integer getSoLuongPhieu();

    @Value("#{target.NGAY_BAT_DAU}")
    LocalDateTime getNgayBatDau();

    @Value("#{target.NGAY_KET_THUC}")
    LocalDateTime getNgayKetThuc();

    @Value("#{target.GIA_TRI_DON_TOI_THIEU}")
    BigDecimal getGiaTriDonToiThieu();

    @Value("#{target.GIA_TRI_GIAM_TOI_DA}")
    BigDecimal getGiaTriGiamToiDa();

    @Value("#{target.TEN_HANG}")
    String getTenhang();

    @Value("#{target.TRANG_THAI}")
    Integer getTrangThai();

    @Value("#{target.NGUOI_TAO}")
    String getNguoiTao();

    @Value("#{target.NGAY_TAO}")
    LocalDateTime getNgayTao();

}

package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonChiTietResponse {
    private Long id;

    private HoaDon hoaDon;

    private BienTheGiay bienTheGiay;

    private BigDecimal donGia;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    private Integer soLuongTra;

    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {

        if (hoaDonChiTiet != null) {
            this.id = hoaDonChiTiet.getId();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setId(hoaDonChiTiet.getHoaDon().getId());
            hoaDon.setKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang());
            this.hoaDon = hoaDon;
            BienTheGiay bienTheGiay = new BienTheGiay();
            bienTheGiay.setId(hoaDonChiTiet.getBienTheGiay().getId());
            bienTheGiay.setHinhAnh(hoaDonChiTiet.getBienTheGiay().getHinhAnh());
            bienTheGiay.setGiay(hoaDonChiTiet.getBienTheGiay().getGiay());
            this.bienTheGiay =bienTheGiay;
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong = hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.ghiChu = hoaDonChiTiet.getGhiChu();

        }
    }
}

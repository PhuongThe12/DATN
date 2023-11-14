package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonChiTietResponse {
    private Long id;

    private HoaDon idHoaDon;

    private BienTheGiay bienTheGiay;

    private Long donGia;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {

        if (hoaDonChiTiet != null) {
            this.id = hoaDonChiTiet.getId();
            this.idHoaDon = hoaDonChiTiet.getIdHoaDon();
            this.bienTheGiay = hoaDonChiTiet.getBienTheGiay();
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong=hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.ghiChu = hoaDonChiTiet.getGhiChu();
        }
    }

}

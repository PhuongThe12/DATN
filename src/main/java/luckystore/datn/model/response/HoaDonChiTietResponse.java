package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDonChiTiet;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonChiTietResponse {

    private Long id;

    private Long idHoaDon;

    private BienTheGiayResponse bienTheGiay;

    private BigDecimal donGia;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {
        if(hoaDonChiTiet != null){
            this.id = hoaDonChiTiet.getId();
            this.idHoaDon = hoaDonChiTiet.getHoaDon().getId();
            this.bienTheGiay = new BienTheGiayResponse(hoaDonChiTiet.getBienTheGiay());
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong = hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.ghiChu = hoaDonChiTiet.getGhiChu();
        }
    }

}

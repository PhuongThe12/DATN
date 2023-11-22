package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDonChiTiet;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Integer soLuongTra;

    private String ghiChu;

    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {
        if(hoaDonChiTiet != null){
            this.id = hoaDonChiTiet.getId();
            this.idHoaDon = hoaDonChiTiet.getHoaDon().getId();
            this.bienTheGiay = new BienTheGiayResponse(hoaDonChiTiet.getBienTheGiay());
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong = hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.soLuongTra = hoaDonChiTiet.getSoLuongTra();
            this.ghiChu = hoaDonChiTiet.getGhiChu();
        }
    }

    public HoaDonChiTietResponse(Long id, Long idHoaDon, BienTheGiayResponse bienTheGiayResponse, Integer soLuong) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.bienTheGiay = bienTheGiayResponse;
        this.soLuong = soLuong;
    }

}
package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.YeuCau;
import luckystore.datn.entity.YeuCauChiTiet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietResponse {

    private Long id;

    private YeuCau yeuCau;

    private HoaDonChiTietResponse hoaDonChiTiet;

    private BienTheGiayResponse bienTheGiay;

    private String lyDo;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    public YeuCauChiTietResponse(YeuCauChiTiet yeuCauChiTiet) {
        if(yeuCauChiTiet != null){
            this.id = yeuCauChiTiet.getId();
            this.yeuCau = yeuCauChiTiet.getYeuCau();
            this.hoaDonChiTiet = new HoaDonChiTietResponse(yeuCauChiTiet.getHoaDonChiTiet());
            this.bienTheGiay = new BienTheGiayResponse(yeuCauChiTiet.getBienTheGiay());
            this.lyDo = yeuCauChiTiet.getLyDo();
            this.soLuong = yeuCauChiTiet.getSoLuong();
            this.trangThai = yeuCauChiTiet.getTrangThai();
            this.ghiChu = yeuCauChiTiet.getGhiChu();
        }
    }
}

package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.YeuCau;
import luckystore.datn.entity.YeuCauChiTiet;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietResponse {

    private Long id;

    private YeuCauResponse yeuCau;

    private HoaDonChiTietResponse hoaDonChiTiet;

    private BienTheGiayResponse bienTheGiay;

    private LyDoResponse lyDo;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    public YeuCauChiTietResponse(YeuCauChiTiet yeuCauChiTiet) {
        if(yeuCauChiTiet != null){
            this.id = yeuCauChiTiet.getId();
            this.yeuCau = new YeuCauResponse(yeuCauChiTiet.getYeuCau());
            this.hoaDonChiTiet = new HoaDonChiTietResponse(yeuCauChiTiet.getHoaDonChiTiet());
            this.bienTheGiay = new BienTheGiayResponse(yeuCauChiTiet.getBienTheGiay());
            this.lyDo = new LyDoResponse(yeuCauChiTiet.getLyDo());
            this.soLuong = yeuCauChiTiet.getSoLuong();
            this.trangThai = yeuCauChiTiet.getTrangThai();
            this.ghiChu = yeuCauChiTiet.getGhiChu();
        }
    }
}

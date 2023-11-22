package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.GioHangChiTiet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GioHangChiTietResponse {

    private Long id;

    private GioHangResponse gioHang;

    private BienTheGiayResponse bienTheGiay;

    private Integer soLuong;

    private BigDecimal giaBan;

    private LocalDateTime ngayTao;

    private String ghiChu;

    public GioHangChiTietResponse(GioHangChiTiet gioHangChiTiet){
        this.id  = gioHangChiTiet.getId();
//        this.gioHang = new GioHangResponse(gioHangChiTiet.getGioHang());
        this.bienTheGiay = new BienTheGiayResponse(gioHangChiTiet.getBienTheGiay());
        this.soLuong = gioHangChiTiet.getSoLuong();
        this.giaBan = gioHangChiTiet.getGiaBan();
        this.ngayTao = gioHangChiTiet.getNgayTao();
        this.ghiChu = gioHangChiTiet.getGhiChu();
    }

}

package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.GioHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GioHangResponse {

    private Long id;

    private KhachHangResponse khachHang;

    private LocalDateTime ngayTao;

    private String ghiChu;

    private Integer trangThai;

    private List<GioHangChiTietResponse> gioHangChiTietResponses ;

    public GioHangResponse(GioHang gioHang){
        this.id = gioHang.getId();
        this.khachHang = new KhachHangResponse(gioHang.getKhachHang());
        this.ngayTao = gioHang.getNgayTao();
        this.ghiChu = gioHang.getGhiChu();
        this.trangThai = gioHang.getTrangThai();
        if(gioHang.getGioHangChiTiets() != null){
            this.gioHangChiTietResponses =(gioHang.getGioHangChiTiets().stream().map(GioHangChiTietResponse::new).collect(Collectors.toList()));
        }
    }

}

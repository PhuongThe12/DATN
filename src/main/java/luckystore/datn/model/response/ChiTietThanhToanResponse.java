package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.entity.ChiTietThanhToan;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChiTietThanhToanResponse {

    private Long id;

    private HoaDonResponse hoaDon;
    private Integer hinhThucThanhToan;
    private BigDecimal tienThanhToan;
    private Integer trangThai;

    private String maGiaoDich;
    private String ghiChu;

    public ChiTietThanhToanResponse(ChiTietThanhToan chiTietThanhToan) {
        this.id = chiTietThanhToan.getId();
        this.hinhThucThanhToan = chiTietThanhToan.getHinhThucThanhToan();
        this.tienThanhToan = chiTietThanhToan.getTienThanhToan();
        this.trangThai = chiTietThanhToan.getTrangThai();
        this.ghiChu = chiTietThanhToan.getGhiChu();
    }
}

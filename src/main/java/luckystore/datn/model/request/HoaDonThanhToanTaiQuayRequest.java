package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonThanhToanTaiQuayRequest {
    private Long idHoaDon;

    private Long idDieuKien;

    private BigDecimal tienGiam;

    private Integer phuongThuc;

    private BigDecimal tienMat;

    private BigDecimal tienChuyenKhoan;

    private String ghiChu;

}

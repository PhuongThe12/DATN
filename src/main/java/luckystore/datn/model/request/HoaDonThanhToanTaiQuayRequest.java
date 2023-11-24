package luckystore.datn.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Không được để trống")
    private String idHoaDon;

    private Long idDieuKien;

    private BigDecimal tienGiam;

    private Integer phuongThuc;

    @Min(value = 0, message = "Tiền không được âm")
    private BigDecimal tienMat;

    @Min(value = 10001, message = "Tiền không được nhỏ hơn 10 nghìn")
    @Max(value = 999999999, message = "Tiền không được lớn hơn 1tỷ")
    private BigDecimal tienChuyenKhoan;

    private String maGiaoDich;

    private String ghiChu;

}

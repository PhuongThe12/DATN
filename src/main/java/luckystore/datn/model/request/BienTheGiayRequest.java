package luckystore.datn.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.PriceCheck;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PriceCheck
public class BienTheGiayRequest {

    @NotNull(message = "Không được để trống")
    private Long mauSacId;

    @NotNull(message = "Không được để trống")
    private Long kichThuocId;

    @NotNull(message = "Không được để trống")
    @DecimalMin(message = "Không được âm", value = "0.0")
    private BigDecimal giaNhap;

    @NotNull(message = "Không được để trống")
    @DecimalMin(message = "Không được âm", value = "0.0")
    private BigDecimal giaBan;

    @NotNull(message = "Không được để trống")
    private Integer trangThai;

    @NotNull(message = "Không được để trống")
    @Length(message = "Không được vượt quá 20 ký tự", max = 20)
    private String barcode;

    @NotNull(message = "Không được để trống")
    private Integer soLuong;

}

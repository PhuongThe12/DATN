package luckystore.datn.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BankingInfoRequest {

    @NotNull(message = "Không được null")
    private Long idHoaDon;

    @NotNull(message = "Không được null")
    @Min(value = 0, message = "Không được âm")
    @Max(value = 999999999, message = "Tiền quá lớn")
    private BigDecimal tongTien;

}

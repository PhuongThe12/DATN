package luckystore.datn.model.request;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DieuKienRequest {

    private Long id;

    @Max(value = 50, message = "Không được vượt quá 50%")
    private Integer phanTramGiam;

    private BigDecimal tongHoaDon;

    private Long DotGiamGiaId;

}
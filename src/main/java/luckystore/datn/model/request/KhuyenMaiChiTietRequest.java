package luckystore.datn.model.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMaiChiTietRequest {

    private Long id;

    private Long bienTheGiayId;

    @NotNull(message = "Không được null")
    @Min(value = 1, message = "Không được < 1")
    @Max(value = 50, message = "Không được > 50")
    private Integer phanTramGiam;

}

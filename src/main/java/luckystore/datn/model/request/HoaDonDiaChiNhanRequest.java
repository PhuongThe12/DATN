package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDiaChiNhanRequest {
    private Long id;

    private String soDienThoaiNhan;

    private String diaChiNhan;

    private BigDecimal phiShip;
}

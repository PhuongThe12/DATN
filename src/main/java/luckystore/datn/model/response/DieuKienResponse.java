package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DieuKien;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DieuKienResponse {

    private Long id;

    private Integer phanTramGiam;

    private BigDecimal tongHoaDon;

    private Long dotGiamGiaId;

    public DieuKienResponse(DieuKien dieuKien) {
        if (dieuKien != null) {
            this.id = dieuKien.getId();
            this.phanTramGiam = dieuKien.getPhanTramGiam();
            this.tongHoaDon = dieuKien.getTongHoaDon();
            this.dotGiamGiaId = dieuKien.getDotGiamGia() == null ? null : dieuKien.getDotGiamGia().getId();
        }
    }

}

package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.LotGiay;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotGiayResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public LotGiayResponse(LotGiay lotGiay) {
        if (lotGiay != null) {
            this.id = lotGiay.getId();
            this.ten = lotGiay.getTen();
            this.moTa = lotGiay.getMoTa();
            this.trangThai = lotGiay.getTrangThai();
        }
    }

}
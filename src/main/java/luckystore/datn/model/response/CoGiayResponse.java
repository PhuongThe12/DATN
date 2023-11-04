package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.CoGiay;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoGiayResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public CoGiayResponse(CoGiay coGiay) {
        if (coGiay != null) {
            this.id = coGiay.getId();
            this.ten = coGiay.getTen();
            this.moTa = coGiay.getMoTa();
            this.trangThai = coGiay.getTrangThai();
        }
    }

}
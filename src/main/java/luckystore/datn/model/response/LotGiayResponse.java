package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.LotGiay;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public LotGiayResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}
package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.MuiGiay;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MuiGiayResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public MuiGiayResponse(MuiGiay muiGiay) {
        if (muiGiay != null) {
            this.id = muiGiay.getId();
            this.ten = muiGiay.getTen();
            this.moTa = muiGiay.getMoTa();
            this.trangThai = muiGiay.getTrangThai();
        }
    }

    public MuiGiayResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}

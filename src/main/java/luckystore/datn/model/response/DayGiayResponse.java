package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DayGiay;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayGiayResponse {

    private Long id;

    private String ten;

    private String mauSac;

    private String moTa;

    private Integer trangThai;

    public DayGiayResponse(DayGiay dayGiay) {
        if (dayGiay != null) {
            this.id = dayGiay.getId();
            this.ten = dayGiay.getTen();
            this.mauSac = dayGiay.getMauSac();
            this.moTa = dayGiay.getMoTa();
            this.trangThai = dayGiay.getTrangThai();
        }
    }

}

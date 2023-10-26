package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DayGiay;
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

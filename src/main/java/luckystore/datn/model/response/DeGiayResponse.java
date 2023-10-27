package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DeGiay;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeGiayResponse {

    private Long id;

    private String ten;

    private String chatLieu;

    private String moTa;

    private Integer trangThai;

    public DeGiayResponse(DeGiay deGiay) {
        if (deGiay != null) {
            this.id = deGiay.getId();
            this.ten = deGiay.getTen();
            this.chatLieu = deGiay.getChatLieu();
            this.moTa = deGiay.getMoTa();
            this.trangThai = deGiay.getTrangThai();
        }
    }

}

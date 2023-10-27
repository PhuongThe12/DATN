package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.MauSac;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MauSacResponse {

    private Long id;

    private String maMau;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public MauSacResponse(MauSac mauSac) {
        if (mauSac != null) {
            this.id = mauSac.getId();
            this.maMau = mauSac.getMaMau();
            this.ten = mauSac.getTen();
            this.moTa = mauSac.getMoTa();
            this.trangThai = mauSac.getTrangThai();
        }
    }

}

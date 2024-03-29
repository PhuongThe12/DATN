package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.MauSac;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public MauSacResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}

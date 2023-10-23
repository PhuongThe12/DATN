package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.MauSac;
import luckystore.datn.entity.ThuongHieu;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThuongHieuResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public ThuongHieuResponse(ThuongHieu thuongHieu) {
        if (thuongHieu != null) {
            this.id = thuongHieu.getId();
            this.ten = thuongHieu.getTen();
            this.moTa = thuongHieu.getMoTa();
            this.trangThai = thuongHieu.getTrangThai();
        }
    }

}
package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.KichThuoc;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KichThuocResponse {

    private Long id;

    private String ten;

    private Double chieuDai;

    private Double chieuRong;

    private String moTa;

    private Integer trangThai;

    public KichThuocResponse(KichThuoc kichThuoc) {
        if (kichThuoc != null) {
            this.id = kichThuoc.getId();
            this.ten = kichThuoc.getTen();
            this.chieuDai = kichThuoc.getChieuDai();
            this.chieuRong = kichThuoc.getChieuRong();
            this.moTa = kichThuoc.getMoTa();
            this.trangThai = kichThuoc.getTrangThai();
        }
    }

    public KichThuocResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}

package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.KhuyenMaiChiTiet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiChiTietResponse {

    private Long id;

    private BienTheGiayResponse bienTheGiayResponsel;

    private Integer phanTramGiam;

    public KhuyenMaiChiTietResponse(KhuyenMaiChiTiet khuyenMaiChiTiet) {
        if (khuyenMaiChiTiet != null) {
            this.id= khuyenMaiChiTiet.getId();
            this.phanTramGiam = khuyenMaiChiTiet.getPhanTramGiam();
            this.bienTheGiayResponsel = new BienTheGiayResponse(khuyenMaiChiTiet.getBienTheGiay());
        }
    }

    public KhuyenMaiChiTietResponse(Long id, Long idBienThe, Integer phanTramGiam) {
        this.id = id;
        this.bienTheGiayResponsel = BienTheGiayResponse.builder().id(idBienThe).build();
        this.phanTramGiam = phanTramGiam;
    }

    public KhuyenMaiChiTietResponse(Long id, Long idBienThe, Integer phanTramGiam, Long idGiay) {
        this.id = id;
        GiayResponse giayResponse = new GiayResponse();
        giayResponse.setId(idGiay);
        this.bienTheGiayResponsel = BienTheGiayResponse.builder().id(idBienThe).giayResponse(giayResponse).build();
        this.phanTramGiam = phanTramGiam;
    }

}

package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.service.impl.ImageHubServiceImpl;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienTheGiayResponse {

    private Long id;

    private Integer soLuong;

    private Integer soLuongLoi;

    private String hinhAnh;

    private BigDecimal giaBan;

    private String barCode;

    private Integer trangThai;

    private MauSacResponse mauSac;

    private KichThuocResponse kichThuoc;

    private GiayResponse giayResponse;

    public BienTheGiayResponse(BienTheGiay bienTheGiay, int ...level) {
        if (bienTheGiay != null) {
            this.id = bienTheGiay.getId();
            this.soLuong = bienTheGiay.getSoLuong();
            this.soLuongLoi = bienTheGiay.getSoLuongLoi();
            this.hinhAnh = bienTheGiay.getHinhAnh();
            this.giaBan = bienTheGiay.getGiaBan();
            this.barCode = bienTheGiay.getBarCode();
            this.trangThai = bienTheGiay.getTrangThai();
            this.mauSac = new MauSacResponse(bienTheGiay.getMauSac());
            this.kichThuoc = new KichThuocResponse(bienTheGiay.getKichThuoc());
            if(bienTheGiay.getGiay() != null && level != null) {
                giayResponse = new GiayResponse();
                giayResponse.setId(bienTheGiay.getGiay().getId());
                giayResponse.setLstAnh(null);
//                giayResponse.getLstAnh().add(bienTheGiay.getGiay().getLstAnh().isEmpty() ? null :
//                        ImageHubServiceImpl.getBase64FromFileStatic(bienTheGiay.getGiay().getLstAnh().get(0).getLink()));
//                giayResponse.setTen(bienTheGiay.getGiay().getTen());
                giayResponse.setLstBienTheGiay(null);
            }
        }
    }

    public BienTheGiayResponse(Long id, String tenKT, String tenMS, Integer soLuong, BigDecimal giaBan) {
        this.id = id;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.mauSac = MauSacResponse.builder().ten(tenMS).build();
        this.kichThuoc = KichThuocResponse.builder().ten(tenKT).build();
    }

    public BienTheGiayResponse(Long id, Long idMauSac, Long idKichThuoc, String barCode) {
        this.id = id;
        this.barCode = barCode;
        this.mauSac = MauSacResponse.builder().id(idMauSac).build();
        this.kichThuoc = KichThuocResponse.builder().id(idKichThuoc).build();
    }

}

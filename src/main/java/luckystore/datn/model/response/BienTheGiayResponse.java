package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienTheGiayResponse {

    private Long id;

    private Integer soLuong;

    private Integer soLuongLoi;

    private String hinhAnh;

    private BigDecimal giaNhap;

    private BigDecimal giaBan;

    private String barCode;

    private Integer trangThai;

    private MauSacResponse mauSac;

    private KichThuocResponse kichthuoc;

    public BienTheGiayResponse(BienTheGiay bienTheGiay) {
        if (bienTheGiay != null) {
            this.id = bienTheGiay.getId();
            this.soLuong = bienTheGiay.getSoLuong();
            this.soLuongLoi = bienTheGiay.getSoLuongLoi();
            this.hinhAnh = bienTheGiay.getHinhAnh();
            this.giaNhap = bienTheGiay.getGiaNhap();
            this.giaBan = bienTheGiay.getGiaBan();
            this.barCode = bienTheGiay.getBarCode();
            this.trangThai = bienTheGiay.getTrangThai();
            this.mauSac = new MauSacResponse(bienTheGiay.getMauSac());
            this.kichthuoc = new KichThuocResponse(bienTheGiay.getKichThuoc());
        }
    }

}

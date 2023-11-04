package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;

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

    private BigDecimal giaNhap;

    private BigDecimal giaBan;

    private String barCode;

    private Integer trangThai;

    private MauSacResponse mauSac;

    private KichThuocResponse kichThuoc;

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
            this.kichThuoc = new KichThuocResponse(bienTheGiay.getKichThuoc());
        }
    }

    public BienTheGiayResponse(Long id, String tenKT, String tenMS, Integer soLuong, BigDecimal giaBan, BigDecimal giaNhap) {
        this.id = id;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.mauSac = MauSacResponse.builder().ten(tenMS).build();
        this.kichThuoc = KichThuocResponse.builder().ten(tenKT).build();
    }

}

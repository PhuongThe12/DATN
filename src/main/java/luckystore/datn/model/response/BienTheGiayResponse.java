package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import luckystore.datn.entity.*;

import java.math.BigDecimal;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BienTheGiayResponse extends BaseBienTheResponse {

    private Long id;

    private Integer soLuong;

    private Integer soLuongLoi;

    private String hinhAnh;

    private BigDecimal giaBan;

    private Integer khuyenMai = 0;

    private String barCode;

    private Integer trangThai;

    private MauSacResponse mauSac;

    private KichThuocResponse kichThuoc;

    private GiayResponse giayResponse;

    private Long soLuongThongKe;

    private Long soLuongMua;

    private Long soLuongTra;

    private Long tyLeTra;

    public BienTheGiayResponse(BienTheGiay bienTheGiay, int... level) {
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
            if (bienTheGiay.getGiay() != null && level != null) {
                giayResponse = new GiayResponse();
                giayResponse.setId(bienTheGiay.getGiay().getId());
                giayResponse.setLstAnh(bienTheGiay.getGiay().getLstAnh().stream().map(HinhAnh::getLink).toList());
                giayResponse.setTen(bienTheGiay.getGiay().getTen());
//                giayResponse.getLstAnh().add(bienTheGiay.getGiay().getLstAnh().isEmpty() ? null :
//                        ImageHubServiceImpl.getBase64FromFileStatic(bienTheGiay.getGiay().getLstAnh().get(0).getLink()));
                giayResponse.setTen(bienTheGiay.getGiay().getTen());
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

    public BienTheGiayResponse(Long id, String tenKT, String tenMS, Integer soLuong, BigDecimal giaBan, String hinhAnh, Integer trangThai, Giay giay, Integer phanTramGiam) {
        this.id = id;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
        this.mauSac = MauSacResponse.builder().ten(tenMS).build();
        this.kichThuoc = KichThuocResponse.builder().ten(tenKT).build();
        this.giayResponse = GiayResponse.builder().ten(giay.getTen()).build();
        this.khuyenMai = phanTramGiam;
    }

    public BienTheGiayResponse(Long id, BigDecimal giaBan, Integer phanTramGiam, Integer trangThai) {
        this.id = id;
        this.giaBan = giaBan;
        this.khuyenMai = phanTramGiam;
        this.trangThai = trangThai;
    }

    public BienTheGiayResponse(Long id, Long idMauSac, Long idKichThuoc, String barCode) {
        this.id = id;
        this.barCode = barCode;
        this.mauSac = MauSacResponse.builder().id(idMauSac).build();
        this.kichThuoc = KichThuocResponse.builder().id(idKichThuoc).build();
    }

    public BienTheGiayResponse(Long id, Integer soLuong) {
        this.id = id;
        this.soLuong = soLuong;
    }

    public BienTheGiayResponse(BienTheGiay bienTheGiay, Giay giay, MauSac mauSac, KichThuoc kichThuoc, Long soLuongThongKe) {
        this.id = bienTheGiay.getId();
        this.giayResponse = new GiayResponse(giay, giay.getTen());
        this.mauSac = new MauSacResponse(mauSac.getId(), mauSac.getTen());
        this.kichThuoc = new KichThuocResponse(kichThuoc.getId(), kichThuoc.getTen());
        this.hinhAnh = bienTheGiay.getHinhAnh();
        this.soLuongThongKe = soLuongThongKe;
    }
    public BienTheGiayResponse(BienTheGiay bienTheGiay, Giay giay, MauSac mauSac, KichThuoc kichThuoc, Long soLuongMua, Long soLuongTra,double tyLeTra) {
        this.id = bienTheGiay.getId();
        this.giayResponse = new GiayResponse(giay, giay.getTen());
        this.mauSac = new MauSacResponse(mauSac.getId(), mauSac.getTen());
        this.kichThuoc = new KichThuocResponse(kichThuoc.getId(), kichThuoc.getTen());
        this.hinhAnh = bienTheGiay.getHinhAnh();
        this.soLuongMua = soLuongMua;
        this.soLuongTra = soLuongTra;
        this.tyLeTra =  Math.round(tyLeTra);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BienTheGiayResponse that = (BienTheGiayResponse) o;
        return Objects.equals(id, that.id);
    }

    public BienTheGiayResponse(Long id, Integer soLuong, BigDecimal giaBan) {
        this.id = id;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

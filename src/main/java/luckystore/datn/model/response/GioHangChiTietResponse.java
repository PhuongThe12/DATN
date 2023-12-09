package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.entity.KhuyenMaiChiTiet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GioHangChiTietResponse {

    private Long id;

    private GioHangResponse gioHang;

    private BienTheGiayResponse bienTheGiay;

    private Integer soLuong;

    private BigDecimal giaBan;

    private LocalDateTime ngayTao;

    private String ghiChu;

    public GioHangChiTietResponse(GioHangChiTiet gioHangChiTiet) {
        this.id = gioHangChiTiet.getId();
//        this.gioHang = new GioHangResponse(gioHangChiTiet.getGioHang());
        this.bienTheGiay = getBienTheGiay(new BienTheGiayResponse(), gioHangChiTiet.getBienTheGiay());
        this.soLuong = gioHangChiTiet.getSoLuong();
        this.giaBan = gioHangChiTiet.getGiaBan();
        this.ngayTao = gioHangChiTiet.getNgayTao();
        this.ghiChu = gioHangChiTiet.getGhiChu();
    }


    public BienTheGiayResponse getBienTheGiay(BienTheGiayResponse bienTheGiayResponse, BienTheGiay bienTheGiay) {
        bienTheGiayResponse.setId(bienTheGiay.getId());
        bienTheGiayResponse.setSoLuong(bienTheGiay.getSoLuong());
        bienTheGiayResponse.setGiaBan(bienTheGiay.getGiaBan());
        bienTheGiayResponse.setHinhAnh(bienTheGiay.getHinhAnh());
        bienTheGiayResponse.setTrangThai(bienTheGiay.getTrangThai());
        bienTheGiayResponse.setMauSac(MauSacResponse.builder().ten(bienTheGiay.getMauSac().getTen()).build());
        bienTheGiayResponse.setKichThuoc(KichThuocResponse.builder().ten(bienTheGiay.getKichThuoc().getTen()).build());
        bienTheGiayResponse.setGiayResponse(GiayResponse.builder().ten(bienTheGiay.getGiay().getTen()).build());
        if(bienTheGiay.getKhuyenMaiChiTietList().size() > 0){
            Integer phanTramGiam = 0;
            for(KhuyenMaiChiTiet khuyenMaiChiTiet : bienTheGiay.getKhuyenMaiChiTietList()){
                phanTramGiam += khuyenMaiChiTiet.getPhanTramGiam();
            }
            bienTheGiayResponse.setKhuyenMai(phanTramGiam);
        }
        return bienTheGiayResponse;
    }

}

package luckystore.datn.model.response;

<<<<<<< HEAD
=======
import com.fasterxml.jackson.annotation.JsonInclude;
>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;

import java.math.BigDecimal;
=======
import luckystore.datn.entity.HoaDonChiTiet;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonChiTietResponse {

    private Long id;

<<<<<<< HEAD
    private HoaDon hoaDon;
=======
    private Long idHoaDon;
>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec

    private BienTheGiayResponse bienTheGiay;

    private BigDecimal donGia;

    private Integer soLuong;

    private Integer trangThai;

    private Integer soLuongTra;

    private String ghiChu;

<<<<<<< HEAD
    private Integer soLuongTra;

    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {

        if (hoaDonChiTiet != null) {
            this.id = hoaDonChiTiet.getId();
            HoaDon hoaDon = new HoaDon();
            hoaDon.setId(hoaDonChiTiet.getHoaDon().getId());
            hoaDon.setKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang());
            this.hoaDon = hoaDon;
            BienTheGiay bienTheGiay = new BienTheGiay();
            bienTheGiay.setId(hoaDonChiTiet.getBienTheGiay().getId());
            bienTheGiay.setHinhAnh(hoaDonChiTiet.getBienTheGiay().getHinhAnh());
            bienTheGiay.setGiay(hoaDonChiTiet.getBienTheGiay().getGiay());
            this.bienTheGiay =bienTheGiay;
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong = hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.ghiChu = hoaDonChiTiet.getGhiChu();

        }
    }
=======
    public HoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet) {
        if(hoaDonChiTiet != null){
            this.id = hoaDonChiTiet.getId();
            this.idHoaDon = hoaDonChiTiet.getHoaDon().getId();
            this.bienTheGiay = new BienTheGiayResponse(hoaDonChiTiet.getBienTheGiay());
            this.donGia = hoaDonChiTiet.getDonGia();
            this.soLuong = hoaDonChiTiet.getSoLuong();
            this.trangThai = hoaDonChiTiet.getTrangThai();
            this.soLuongTra = hoaDonChiTiet.getSoLuongTra();
            this.ghiChu = hoaDonChiTiet.getGhiChu();
        }
    }

    public HoaDonChiTietResponse(Long id, Long idHoaDon, BienTheGiayResponse bienTheGiayResponse, Integer soLuong) {
        this.id = id;
        this.idHoaDon = idHoaDon;
        this.bienTheGiay = bienTheGiayResponse;
        this.soLuong = soLuong;
    }

>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec
}

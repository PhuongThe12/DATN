package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonResponse {
    private Long id;

    private HoaDon hoaDonGoc;

    private KhachHang khachHang;

    private NhanVien nhanVien;

    private String ngayTao;

    private String ngayShip;

    private String ngayNhan;

    private String ngayThanhToan;

    private Integer kenhBan;

    private String maVanDon;

    private String email;

    private BigDecimal phiShip;

    private String soDienThoaiNhan;

    private String diaChiNhan;

    private Integer trangThai;

    private String ghiChu;

    private String dateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public HoaDonResponse(HoaDon hoaDon) {

        if (hoaDon != null) {
            this.id = hoaDon.getId();
            this.hoaDonGoc = hoaDon.getHoaDonGoc();
            this.nhanVien = hoaDon.getNhanVien();
            this.khachHang = hoaDon.getKhachHang();
            this.ngayTao = dateFormat(hoaDon.getNgayTao());
            this.ngayShip = dateFormat(hoaDon.getNgayShip());
            this.ngayNhan = dateFormat(hoaDon.getNgayNhan());
            this.ngayThanhToan = dateFormat(hoaDon.getNgayThanhToan());
            this.kenhBan = hoaDon.getKenhBan();
            this.maVanDon = hoaDon.getMaVanDon().trim();
            this.email = hoaDon.getEmail().trim();
            this.phiShip = hoaDon.getPhiShip();
            this.soDienThoaiNhan = hoaDon.getSoDienThoaiNhan().trim();
            this.diaChiNhan = hoaDon.getDiaChiNhan().trim();
            this.trangThai = hoaDon.getTrangThai();
            this.ghiChu = hoaDon.getGhiChu();
        }
    }
}

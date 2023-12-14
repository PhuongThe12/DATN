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

    private Long hoaDonGoc;

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

    private BigDecimal tienGiam;

    private Integer uuDai;

    private Integer loaiHoaDon;


    public HoaDonResponse(HoaDon hoaDon) {

        if (hoaDon != null) {
            this.id = hoaDon.getId();
            this.hoaDonGoc = hoaDon.getHoaDonGoc();
            this.nhanVien = hoaDon.getNhanVien();
            this.khachHang = hoaDon.getKhachHang();

            if (hoaDon.getNgayTao() != null) {
                this.ngayTao = dateFormat(hoaDon.getNgayTao());
            }
            if (hoaDon.getNgayShip() != null) {
                this.ngayShip = dateFormat(hoaDon.getNgayShip());
            }
            if (hoaDon.getNgayNhan() != null) {
                this.ngayNhan = dateFormat(hoaDon.getNgayNhan());
            }
            if (hoaDon.getNgayThanhToan() != null) {
                this.ngayThanhToan = dateFormat(hoaDon.getNgayThanhToan());
            }

            this.tienGiam = hoaDon.getTienGiam();
            this.uuDai = hoaDon.getUuDai();
            this.kenhBan = hoaDon.getKenhBan();
            this.maVanDon = hoaDon.getMaVanDon();
            this.email = hoaDon.getEmail();
            this.phiShip = hoaDon.getPhiShip();
            this.soDienThoaiNhan = hoaDon.getSoDienThoaiNhan();
            this.diaChiNhan = hoaDon.getDiaChiNhan();
            this.trangThai = hoaDon.getTrangThai();
            this.ghiChu = hoaDon.getGhiChu();
            this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        }
    }

    public HoaDonResponse(HoaDon hoaDon, int level) {

        if (hoaDon != null) {
            this.id = hoaDon.getId();
            this.hoaDonGoc = hoaDon.getHoaDonGoc();
            this.nhanVien = hoaDon.getNhanVien();
            this.khachHang = hoaDon.getKhachHang();

            if (hoaDon.getNgayTao() != null) {
                this.ngayTao = dateFormat(hoaDon.getNgayTao());
            }
            if (hoaDon.getNgayShip() != null) {
                this.ngayShip = dateFormat(hoaDon.getNgayShip());
            }
            if (hoaDon.getNgayNhan() != null) {
                this.ngayNhan = dateFormat(hoaDon.getNgayNhan());
            }
            if (hoaDon.getNgayThanhToan() != null) {
                this.ngayThanhToan = dateFormat(hoaDon.getNgayThanhToan());
            }

            this.tienGiam = hoaDon.getTienGiam();
            this.uuDai = hoaDon.getUuDai();
            this.kenhBan = hoaDon.getKenhBan();
            this.maVanDon = hoaDon.getMaVanDon();
            this.email = hoaDon.getEmail();
            this.phiShip = hoaDon.getPhiShip();
            this.soDienThoaiNhan = hoaDon.getSoDienThoaiNhan();
            this.diaChiNhan = hoaDon.getDiaChiNhan();
            this.trangThai = hoaDon.getTrangThai();
            this.ghiChu = hoaDon.getGhiChu();
            this.loaiHoaDon = hoaDon.getLoaiHoaDon();

        }
    }

    private String dateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }


}

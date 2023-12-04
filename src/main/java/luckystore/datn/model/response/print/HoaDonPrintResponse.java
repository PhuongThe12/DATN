package luckystore.datn.model.response.print;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.model.response.KichThuocResponse;
import luckystore.datn.model.response.MauSacResponse;
import luckystore.datn.model.response.NhanVienResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class HoaDonPrintResponse {

    private Long id;

    private Long hoaDonGoc;

    private KhachHangResponse khachHang;

    private NhanVienResponse nhanVien;

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

    private List<HoaDonChiTietResponse> hoaDonChiTietResponses = new ArrayList<>();

    private Set<ChiTietThanhToan> chiTietThanhToans;

    public HoaDonPrintResponse(HoaDon hoaDon) {

        if (hoaDon != null) {
            this.id = hoaDon.getId();
            if (hoaDon.getNhanVien() != null) {
                this.nhanVien = new NhanVienResponse();
                this.nhanVien.setHoTen(hoaDon.getNhanVien().getHoTen());
            }
            if (hoaDon.getKhachHang() != null) {
                this.khachHang = new KhachHangResponse();
                this.khachHang.setHoTen(hoaDon.getKhachHang().getHoTen());
            }

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

            this.hoaDonGoc = hoaDon.getHoaDonGoc();
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
            hoaDon.getListHoaDonChiTiet().forEach(item -> {
                HoaDonChiTietResponse response = new HoaDonChiTietResponse();
                response.setId(item.getId());
                response.setDonGia(item.getDonGia());
                response.setSoLuong(item.getSoLuong());
                BienTheGiayResponse bienTheGiayResponse = new BienTheGiayResponse();
                bienTheGiayResponse.setGiaBan(item.getBienTheGiay().getGiaBan());
                bienTheGiayResponse.setKichThuoc(KichThuocResponse.builder().ten(item.getBienTheGiay().getKichThuoc().getTen()).build());
                bienTheGiayResponse.setMauSac(MauSacResponse.builder().ten(item.getBienTheGiay().getMauSac().getTen()).build());
                bienTheGiayResponse.setGiayResponse(GiayResponse.builder().ten(item.getBienTheGiay().getGiay().getTen()).build());
                response.setBienTheGiay(bienTheGiayResponse);

                this.hoaDonChiTietResponses.add(response);
            });
            this.chiTietThanhToans = hoaDon.getChiTietThanhToans();
        }
    }

    public HoaDonPrintResponse(HoaDon hoaDon, int level) {

        if (hoaDon != null) {
            this.id = hoaDon.getId();
            this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien());
            this.khachHang = new KhachHangResponse(hoaDon.getKhachHang());

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

            this.hoaDonGoc = hoaDon.getHoaDonGoc();
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
            this.hoaDonChiTietResponses = hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).toList();
            this.chiTietThanhToans = hoaDon.getChiTietThanhToans();
            this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        }
    }

    private String dateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

}

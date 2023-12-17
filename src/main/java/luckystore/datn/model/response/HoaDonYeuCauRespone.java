package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.HoaDon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonYeuCauRespone {

    private Long id;

    private Long hoaDonGoc;

    private KhachHangResponse khachHang;

    private NhanVienResponse nhanVien;

    private Integer kenhBan;

    private Integer trangThai;

    private Integer loaiHoaDon;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayShip;

    private LocalDateTime ngayNhan;

    private LocalDateTime ngayThanhToan;
    private String ghiChu;

    private String soDienThoaiNhan;

    private String diaChiNhan;
    private List<ChiTietThanhToanResponse> listChiTietThanhToan = new ArrayList<>();
    private List<HoaDonChiTietResponse> listHoaDonChiTiet = new ArrayList<>();
    private BigDecimal tongTienKhachThanhToan;
    private BigDecimal tongGiaTriHoaDon;

    private Double phanTramGiam;

    public HoaDonYeuCauRespone(HoaDon hoaDon, Long idHoaDonGoc) {
        this.id = hoaDon.getId();
        this.hoaDonGoc = idHoaDonGoc;
        this.khachHang = new KhachHangResponse(hoaDon.getKhachHang().getId(), hoaDon.getKhachHang().getHoTen());
        this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien().getId(), hoaDon.getNhanVien().getHoTen());
        this.kenhBan = hoaDon.getKenhBan();
        this.trangThai = hoaDon.getTrangThai();
        this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        this.ngayTao = hoaDon.getNgayTao();
        this.ngayShip = hoaDon.getNgayShip();
        this.ngayNhan = hoaDon.getNgayNhan();
        this.ngayThanhToan = hoaDon.getNgayThanhToan();
        this.ghiChu = hoaDon.getGhiChu();
        this.listHoaDonChiTiet = (hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList()));
        this.tongTienKhachThanhToan = tongTienKhachThanhToan((hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList())));
        this.tongGiaTriHoaDon = hoaDon.getTienGiam().add(tongTienKhachThanhToan(hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList())));
    }

    public HoaDonYeuCauRespone(HoaDon hoaDon, String getAllYeuCauPage) {
        this.id = hoaDon.getId();
        this.hoaDonGoc = hoaDon.getHoaDonGoc() == null ? null : hoaDon.getHoaDonGoc();
        this.khachHang = new KhachHangResponse(hoaDon.getKhachHang().getId(), hoaDon.getKhachHang().getHoTen(),hoaDon.getKhachHang().getSoDienThoai(),hoaDon.getKhachHang().getEmail());
        this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien().getId(), hoaDon.getNhanVien().getHoTen());
        this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        this.ngayTao = hoaDon.getNgayTao();
        this.kenhBan = hoaDon.getKenhBan();
        this.trangThai = hoaDon.getTrangThai();
        this.tongTienKhachThanhToan = tongTienKhachThanhToan((hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList())));
    }


    public HoaDonYeuCauRespone(HoaDon hoaDon){
        this.id = hoaDon.getId();
        this.hoaDonGoc = hoaDon.getHoaDonGoc();
        this.khachHang = new KhachHangResponse(hoaDon.getKhachHang());
        this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien());
        this.ngayTao = hoaDon.getNgayTao();
        this.trangThai = hoaDon.getTrangThai();
        this.listHoaDonChiTiet = hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList());
        this.listChiTietThanhToan = hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList());
        this.tongTienKhachThanhToan = tongTienKhachThanhToan(hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList()));
        this.tongGiaTriHoaDon = hoaDon.getTienGiam().add(tongTienKhachThanhToan(hoaDon.getChiTietThanhToans().stream().map(ChiTietThanhToanResponse::new).collect(Collectors.toList())));
        this.phanTramGiam = tinhPhanTramGiam(this.tongGiaTriHoaDon,hoaDon.getTienGiam());
        this.soDienThoaiNhan = hoaDon.getSoDienThoaiNhan();
        this.diaChiNhan = hoaDon.getDiaChiNhan();
    }

    private BigDecimal tongTienKhachThanhToan(List<ChiTietThanhToanResponse> listChiTietThanhToan) {
        BigDecimal result = BigDecimal.ZERO;
        if (listChiTietThanhToan != null) { // Kiểm tra xem list không phải là null
            for (ChiTietThanhToanResponse cttt : listChiTietThanhToan) {
                if (cttt != null && cttt.getTienThanhToan() != null) { // Kiểm tra xem đối tượng và giá trị tiền không phải là null
                    result = result.add(cttt.getTienThanhToan());
                }
            }
        }
        return result;
    }

    private Double tinhPhanTramGiam(BigDecimal tongGiaTriHoaDon, BigDecimal tienGiam){
        if (tongGiaTriHoaDon == null || tienGiam == null || BigDecimal.ZERO.compareTo(tongGiaTriHoaDon) == 0) {
            // Xử lý trường hợp tổng giá trị hóa đơn là 0 hoặc null
            return null;
        }
        BigDecimal phanTram = tienGiam.multiply(new BigDecimal("100")).divide(tongGiaTriHoaDon, 2, RoundingMode.HALF_UP);
        return phanTram.doubleValue(); // Chuyển đổi sang Integer
    }

}

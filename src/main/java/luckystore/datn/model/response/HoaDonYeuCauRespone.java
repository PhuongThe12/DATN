package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonYeuCauRespone {

    private Long id;

    private Long hoaDonGoc;

    private KhachHangRestponse khachHang;

    private NhanVienResponse nhanVien;

    private Integer kenhBan;

    private Integer trangThai;

    private String moTa;

    private List<HoaDonChiTietResponse> listHoaDonChiTiet = new ArrayList<>();

    private BigDecimal tongTien;

    public HoaDonYeuCauRespone(HoaDon hoaDon) {
        this.id = hoaDon.getId();
        this.hoaDonGoc = hoaDon.getHoaDonGoc().getId();
        this.khachHang = new KhachHangRestponse(hoaDon.getKhachHang().getId(), hoaDon.getKhachHang().getHoTen());
        this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien().getId(), hoaDon.getNhanVien().getHoTen());
        this.kenhBan = hoaDon.getKenhBan();
        this.trangThai = hoaDon.getTrangThai();
        this.moTa = hoaDon.getGhiChu();
        this.listHoaDonChiTiet = (hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList()));
    }


}

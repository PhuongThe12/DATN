package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    private String moTa;

    private List<HoaDonChiTietResponse> listHoaDonChiTiet = new ArrayList<>();

    private BigDecimal tongTien;

    public HoaDonYeuCauRespone(HoaDon hoaDon, Long idHoaDonGoc) {
        this.id = hoaDon.getId();
        this.hoaDonGoc = idHoaDonGoc;
        this.khachHang = new KhachHangResponse(hoaDon.getKhachHang().getId(), hoaDon.getKhachHang().getHoTen());
        this.nhanVien = new NhanVienResponse(hoaDon.getNhanVien().getId(), hoaDon.getNhanVien().getHoTen());
        this.kenhBan = hoaDon.getKenhBan();
        this.trangThai = hoaDon.getTrangThai();
        this.loaiHoaDon = hoaDon.getLoaiHoaDon();
        this.moTa = hoaDon.getGhiChu();
        this.listHoaDonChiTiet = (hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList()));
        this.tongTien = tongTien((hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList())));
    }


    private BigDecimal tongTien(List<HoaDonChiTietResponse> listHoaDonChiTiet){
        BigDecimal result = BigDecimal.ZERO; // Sử dụng BigDecimal.ZERO thay vì new BigDecimal("0")
        for (HoaDonChiTietResponse hdctr : listHoaDonChiTiet) {
            // Thực hiện phép nhân và cộng sử dụng các phương thức của BigDecimal
            BigDecimal thanhTien = new BigDecimal(hdctr.getSoLuong()).multiply(hdctr.getDonGia());
            result = result.add(thanhTien);
        }
        return result;
    }

}

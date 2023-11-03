package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HashTag;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonResponse {
    private Long id;

    private Long idHoaDonGoc;

    private NhanVien nhanVien;

    private KhachHang khachHang;

    private Long tongTien;

    private Date ngayTao;

    private Integer kenhBan;

    private Integer trangThai;

    public HoaDonResponse(HoaDon hoaDon) {
        if (hoaDon != null) {
            this.id = hoaDon.getId();
            this.nhanVien = hoaDon.getNhanVien();
            this.khachHang = hoaDon.getKhachHang();
            this.tongTien = hoaDon.getTongTien();
            this.ngayTao = hoaDon.getNgayTao();
            this.kenhBan = hoaDon.getKenhBan();
            this.trangThai = hoaDon.getTrangThai();
        }
    }
}

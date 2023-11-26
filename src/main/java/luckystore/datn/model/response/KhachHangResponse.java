package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhachHangResponse {
    private Long id;

    private String hoTen;

    private Boolean gioiTinh;

    private Date ngaySinh;

    private String soDienThoai;

    private String email;

    private Integer diemTichLuy;

    private Integer trangThai;

    private HangKhachHang hangKhachHang;

    private TaiKhoan taiKhoan;

    public KhachHangResponse(KhachHang khachHang) {
        if (khachHang != null) {
            this.id = khachHang.getId();
            this.hoTen = khachHang.getHoTen();
            this.gioiTinh= khachHang.getGioiTinh();
            this.ngaySinh = khachHang.getNgaySinh();
            this.soDienThoai = khachHang.getSoDienThoai();
            this.email = khachHang.getEmail();
            this.diemTichLuy = khachHang.getDiemTichLuy();
            this.trangThai = khachHang.getTrangThai();
            this.hangKhachHang=khachHang.getHangKhachHang();
            this.taiKhoan = khachHang.getTaiKhoan();
        }
    }

    public KhachHangResponse(Long id, String ten) {
        this.id = id;
        this.hoTen = ten;
    }
}

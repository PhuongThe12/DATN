package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.TaiKhoan;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanResponse {
    private Long id;

    private String tenDangNhap;

    private String matKhau;

    private Integer trangThai;

    private Integer role;

    public TaiKhoanResponse(TaiKhoan taiKhoan) {
        if (taiKhoan != null) {
            this.id = taiKhoan.getId();
            this.tenDangNhap = taiKhoan.getTenDangNhap();
            this.matKhau = taiKhoan.getMatKhau();
            this.trangThai = taiKhoan.getTrangThai();
            this.role = taiKhoan.getRole();
        }
    }
}

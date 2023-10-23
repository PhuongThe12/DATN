package luckystore.datn.model.response;

import luckystore.datn.entity.TaiKhoan;

public class TaiKhoanResponse {
    private Long id;

    private String tenDangNhap;

    private String matKhau;

    private Integer trangThai;

    public TaiKhoanResponse(TaiKhoan taiKhoan) {
        if (taiKhoan != null) {
            this.id = taiKhoan.getId();
            this.tenDangNhap = taiKhoan.getTenDangNhap();
            this.matKhau = taiKhoan.getMatKhau();
            this.trangThai = taiKhoan.getTrangThai();
        }
    }
}

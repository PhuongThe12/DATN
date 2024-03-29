package luckystore.datn.infrastructure.security.auth;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public class SignupRequest {

    private Long idTaiKhoan;

    @NotNull(message = "Không được để trống tên đăng nhập")
    private String tenDangNhap;

    @NotNull(message = "Không được để trống tên mật khẩu")
    private String matKhau;

    @NotNull(message = "Không được để trống tên mật khẩu")
    private String sdt;

    private Boolean gioiTinh;

    private Date ngaySinh;
}

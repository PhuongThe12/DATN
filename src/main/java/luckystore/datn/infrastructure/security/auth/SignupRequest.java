package luckystore.datn.infrastructure.security.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import luckystore.datn.infrastructure.Role;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SignupRequest {

    @NotBlank(message = "Tên đăng nhập trống")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu trống")
    @Pattern(regexp = "^(?=.*[0-9])(.{8,})$", message = "Mật khẩu phải có ít nhất 8 ký tự và chứa ít nhất 1 số")
    private String matKhau;

    private Role roles;

}

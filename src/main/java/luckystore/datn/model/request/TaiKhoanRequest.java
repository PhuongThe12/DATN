package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.infrastructure.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoanRequest {
    private Long id;

    @NotNull(message = "Không được để trống tên đăng nhập")
    private String tenDangNhap;

    @NotNull(message = "Không được để trống tên mật khẩu")
    private String matKhau;

    private Integer trangThai;

    private Role role;

}

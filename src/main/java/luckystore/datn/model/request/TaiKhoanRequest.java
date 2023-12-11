package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.infrastructure.constants.Role;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoanRequest {
    private Long id;

    @NotNull(message = "Không được để trống tên đăng nhập")
    @Length(message = "Tên đăng nhập không được vượt quá 30 ký tự", max = 30)
    private String tenDangNhap;

    @NotNull(message = "Không được để trống mật khẩu")
    @Length(message = "Độ dài mật khẩu nằm trong khoảng 5- 20 ký tự", min = 5)
    @Length(message = "Độ dài mật khẩu nằm trong khoảng 5- 20 ký tự", max = 20)
    private String matKhau;

    private Integer trangThai;

    private Role role;

}

package luckystore.datn.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NhanVienRequest {
    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 100 ký tự", min = 1 , max = 100)
    private String hoTen;

    @NotNull(message = "Không được để trống giới tính")
    private Integer gioiTinh;

    private Date ngaySinh;

    @Pattern(regexp = "^(0[0-9]{9}|84[0-9]{8})$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Length(message = "Ghi chú không được vượt quá 255 ký tự",max = 255)
    private String ghiChu;

    @NotNull(message = "Không được để trống trạng Thái")
    private Integer trangThai;

    @NotNull(message = "Không được để trống xã")
    private String xa;

    @NotNull(message = "Không được để trống huyện")
    private String huyen;

    @NotNull(message = "Không được để trống tỉnh")
    private String tinh;

    @NotNull(message = "Không được để trống chức vụ")
    private Integer chucVu;

    private Long idTaiKhoan;

    @NotNull(message = "Không được để trống tên đăng nhập")
    private String tenDangNhap;

    @NotNull(message = "Không được để trống mật khẩu")
    private String matKhau;

    private Integer role;
}

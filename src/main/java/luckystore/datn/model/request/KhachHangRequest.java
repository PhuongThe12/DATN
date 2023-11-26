package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.TaiKhoan;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhachHangRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 50 ký tự")
    private String hoTen;

    private Boolean gioiTinh;

    private Date ngaySinh;

    private String soDienThoai;

    private String email;

    private Integer diemTichLuy;

    private Integer trangThai;

    private HangKhachHang hangKhachHang;

    private TaiKhoan taiKhoan;
}

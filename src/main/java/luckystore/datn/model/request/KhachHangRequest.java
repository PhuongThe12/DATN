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
    @Length(message = "Tên không được vượt quá 100 ký tự", max = 100)
    private String hoTen;

    private Boolean gioiTinh;

    private Date ngaySinh;

    private String soDienThoai;

    private String email;

    private String password;

    private Integer diemTichLuy = 0;

    private Integer trangThai = 1;

    private HangKhachHangRequest hangKhachHang;

}

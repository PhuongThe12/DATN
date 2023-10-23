package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HangKhachHangRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên hạng")
    @Length(message = "Tên hạng không được vượt quá 50 ký tự")
    private String tenHang;

    @NotNull(message = "Không được để trống điều kiện")
    private Integer dieuKien;

    @NotNull(message = "Không được để trống ưu đãi")
    private Integer uuDai;

    @NotNull(message = "Không được để trống mô tả")
    private String moTa;

    private Integer trangThai;
}

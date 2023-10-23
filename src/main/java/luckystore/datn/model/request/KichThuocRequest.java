package luckystore.datn.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class KichThuocRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 50 ký tự")
    private String ten;

    @NotNull(message = "Không được để trống chiều rộng")
    @Min(message = "Chiều rộng không được âm", value = 0)
    @Max(message = "Chiều rộng không hợp lệ, chiều rộng không được vượt quá 100", value = 100)
    private Float chieuRong;

    @NotNull(message = "Không được để trống chiều dài")
    @Min(message = "Chiều dài không được âm", value = 0)
    @Max(message = "Chiều dài không hợp lệ, chiều dài không được vượt quá 100", value = 100)
    private Float chieuDai;

    private String moTa;

    private Integer trangThai;
}

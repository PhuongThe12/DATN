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
public class LotGiayRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 100 ký tự", max = 100)
    private String ten;

    @NotNull(message = "Không được để trống mô tả")
    @Length(message = "Mô tả không được quá ngắn", min = 3)
    @Length(message = "Mô tả không được vượt quá 3000 ký tự", max = 3000)
    private String moTa;

    private Integer trangThai;
}
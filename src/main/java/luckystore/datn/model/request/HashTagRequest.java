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
public class HashTagRequest {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 100 ký tự", max = 100)
    private String ten;

    private String moTa;

    private Integer trangThai;
}

package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MauSacRequest {

    private Long id;

    @NotNull(message = "Không được để trống màu sắc")
    @Pattern(regexp = "#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})", message = "Màu sắc không đúng dịnh dạng")
    private String maMau;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 100 ký tự", max = 100)
    private String ten;

    private String moTa;

    private Integer trangThai;
}

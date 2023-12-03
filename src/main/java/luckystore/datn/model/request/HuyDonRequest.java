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
public class HuyDonRequest {

    @NotNull(message = "Không được để trống mã hóa đơn")
    private Long id;

    @NotNull(message = "Không được để trống ghi chú")
    @Length(max = 300, message = "Không được vượt quá 300 ký tự")
    private String ghiChu;
    
    
}

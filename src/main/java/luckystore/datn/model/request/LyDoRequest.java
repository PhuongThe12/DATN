package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyDoRequest {
    private Long id;

    @NotNull(message = "Không được để trống tên lý do")
    private String ten;

    private Integer trangThai;
}

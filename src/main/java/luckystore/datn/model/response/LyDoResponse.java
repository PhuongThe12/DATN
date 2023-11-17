package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.LyDo;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyDoResponse {
    private Long id;

    private String ten;

    private Integer trangThai;

    public LyDoResponse(LyDo lyDo) {
        this.id = lyDo.getId();
        this.ten = lyDo.getTen();
        this.trangThai = lyDo.getTrangThai();
    }
}

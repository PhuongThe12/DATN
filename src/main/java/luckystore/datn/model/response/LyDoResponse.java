package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.LyDo;
import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.repository.LyDoRepository;
import luckystore.datn.repository.YeuCauRepository;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LyDoResponse {
    private LyDoRepository lyDoRepository;
    private Long id;

    private String ten;

    private Integer trangThai;
    private Long soLuongThongKe;

    private Long tyLe;

    private Long soLuongYeuCauTra;

    public LyDoResponse(LyDo lyDo) {
        this.id = lyDo.getId();
        this.ten = lyDo.getTen();
        this.trangThai = lyDo.getTrangThai();
    }
    public LyDoResponse(LyDo lyDo, Long soLuongThongKe) {
        this.id = lyDo.getId();
        this.ten = lyDo.getTen();
        this.soLuongThongKe = soLuongThongKe;
    }
}

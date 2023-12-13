package luckystore.datn.model.request;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DotGiamGiaRequest {

    private String ten;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    @Valid
    private List<DieuKienRequest> dieuKienRequests;

}


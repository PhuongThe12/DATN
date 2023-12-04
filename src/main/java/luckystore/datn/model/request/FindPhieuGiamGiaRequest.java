package luckystore.datn.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FindPhieuGiamGiaRequest extends PageableRequest {

    private String maPhieu;

    private String tenHangKH;

    private Integer phanTramGiam;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;
}

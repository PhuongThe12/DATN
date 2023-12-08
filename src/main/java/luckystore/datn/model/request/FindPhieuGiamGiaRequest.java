package luckystore.datn.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FindPhieuGiamGiaRequest extends PageableRequest {

    private String maGiamGia;

    private String hangKhachHang;

    private Long ngayBatDau;

    private Long ngayKetThuc;

    private Integer trangThai;
}

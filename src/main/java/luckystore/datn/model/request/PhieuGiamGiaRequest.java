package luckystore.datn.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PhieuGiamGiaRequest {

    private String maPhieu;

    private Integer phanTramGiam;

    private Integer soLuongPhieu;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private BigDecimal giaTriDonToiThieu;

    private BigDecimal giaTriGiamToiDa;

    private Long hangKhachHangId;

    private Long nhanVienId;

    private LocalDateTime ngayTao;

    private Integer trangThai;


}

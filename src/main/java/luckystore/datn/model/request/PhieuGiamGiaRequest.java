package luckystore.datn.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PhieuGiamGiaRequest {

    private String maGiamGia;

    private Integer phanTramGiam;

    private Integer soLuongPhieu;

    private Long ngayBatDau;

    private Long ngayKetThuc;

    private BigDecimal giaTriDonToiThieu;

    private BigDecimal giaTriGiamToiDa;

    private String hangKhachHang;

    private Long nhanVienId;

    private LocalDateTime ngayTao;

    private Integer trangThai;


}

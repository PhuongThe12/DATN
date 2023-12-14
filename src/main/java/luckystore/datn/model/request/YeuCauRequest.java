package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauRequest {

    private Long id;

    private Long nguoiThucHien;

    private Long hoaDon;

    private Integer trangThai;

    private String ghiChu;

    private LocalDateTime ngaySua;

    private LocalDateTime ngayTao;

    private Long nguoiTao;

    private Long nguoiSua;

    private String thongTinNhanHang;

    private BigDecimal phiShip;

    private BigDecimal tienKhachTra;

    private List<YeuCauChiTietRequest> listYeuCauChiTiet;

}

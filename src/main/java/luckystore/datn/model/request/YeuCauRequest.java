package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauRequest {

    private Long id;

    private Long nguoiThucHien;

    @NotNull(message = "Không được để trống hóa đơn")
    private HoaDon hoaDon;

    @NotNull(message = "Không được để trống loại yêu cầu")
    private Integer loaiYeuCau;

    @NotNull(message = "Không được để trống trạng thái")
    private Integer trangThai;

    private Date ngayTao;

    private Date ngaySua;

    private String ghiChu;

}

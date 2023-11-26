package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGroup;

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

    private Date ngaySua;

    private Date ngayTao;

    private List<YeuCauChiTietRequest> listYeuCauChiTiet;

}

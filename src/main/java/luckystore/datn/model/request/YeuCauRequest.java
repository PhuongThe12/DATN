package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauRequest {

    private Long id;

    private Long nguoiThucHien;

    private Long hoaDon;

    private Integer loaiYeuCau;

    private Integer trangThai;

    private String ghiChu;

    private List<YeuCauChiTietRequest> listYeuCauChiTiet;

}

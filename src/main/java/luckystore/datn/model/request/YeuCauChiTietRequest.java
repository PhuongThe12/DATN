package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.YeuCau;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietRequest {

    private Long id;

    private YeuCau yeuCau;

    private Long hoaDon;

    private Long giayChiTiet;

    private String lyDo;

    private Integer soLuong;

    private String hinhAnh;

    private Integer trangThai;

    private String ghiChu;
}

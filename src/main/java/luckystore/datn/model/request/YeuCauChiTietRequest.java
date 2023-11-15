package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietRequest {

    private Long id;

    private Long hoaDonChiTiet;

    private Long bienTheGiay;

    private String lyDo;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;
}

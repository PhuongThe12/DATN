package luckystore.datn.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonChiTietRequest {
    private Long id;

    private HoaDon idHoaDon;

    private BienTheGiay bienTheGiay;

    private Long donGia;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;
}

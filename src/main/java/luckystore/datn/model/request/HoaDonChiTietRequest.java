package luckystore.datn.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class HoaDonChiTietRequest {

    private Long id;

    private HoaDon hoaDon;

    private BienTheGiay bienTheGiay;

    private BigDecimal donGia;

    private Integer soLuong;

    private Integer trangThai;

    private String ghiChu;

    private Integer soLuongTra;
}

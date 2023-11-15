package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonSearch {

    private Long idHoaDon;

    private Long idHoaDonGoc;

    private String khachHang;

    private String nhanVien;

    private Date ngayTao;

    private Date ngayShip;

    private Date ngayNhan;

    private Date ngayThanhToan;

    private Integer kenhBan;

    private String maVanDon;

    private String email;

    private BigDecimal phiShip;

    private String soDienThoaiNhan;

    private String diaChiNhan;

    private Integer trangThai;

    private String ghiChu;

    private BigDecimal giaTu;

    private BigDecimal giaDen;
}

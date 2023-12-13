package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonSearch {

    private Long idHoaDon;

    private Long idHoaDonGoc;

    private String tenKhachHang;

    private Long idNhanVien;

    private Date ngayTao;

    private Date ngayShip;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private Date ngayNhan;

    private Date ngayThanhToan;

    private Integer kenhBan;

    private String maVanDon;

    private String email;

    private BigDecimal phiShip;

    private String soDienThoaiNhan;

    private String soDienThoaiKhachHang;

    private String diaChiNhan;

    private Integer trangThai;

    private String ghiChu;

    private BigDecimal giaTu;

    private BigDecimal giaDen;

    private Integer currentPage = 1;

    private Integer pageSize = 5;

    private Integer loaiHoaDon;

    private BigDecimal tongThanhToanMin;

    private BigDecimal tongThanhToanMax;
}

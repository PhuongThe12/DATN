package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonRequest {

    private Long id;

    private HoaDon hoaDonGoc;

    private KhachHangRequest khachHang;

    private NhanVienRequest nhanVien;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayShip;

    private LocalDateTime ngayNhan;

    private LocalDateTime ngayThanhToan;

    private Integer kenhBan;

    private Integer loaiHoaDon;

    private String maVanDon;

    private String email;

    private BigDecimal phiShip;

    private String soDienThoaiNhan;

    private String diaChiNhan;

    private Integer trangThai;

    private String ghiChu;

    private Set<HoaDonChiTietRequest> listHoaDonChiTiet;
}

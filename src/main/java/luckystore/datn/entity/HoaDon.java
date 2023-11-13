package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "HoaDon")
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ID_HOA_DON_GOC")
    private HoaDon hoaDonGoc;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_NHAN_VIEN")
    private NhanVien nhanVien;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

    @Column(name = "NGAY_SHIP")
    private LocalDateTime ngayShip;

    @Column(name = "NGAY_NHAN")
    private LocalDateTime ngayNhan;

    @Column(name = "NGAY_THANH_TOAN")
    private LocalDateTime ngayThanhToan;

    @Column(name = "KENH_BAN")
    private Integer kenhBan;

    @Column(name = "MA_VAN_DON")
    private String maVanDon;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHI_SHIP")
    private BigDecimal phiShip;

    @Column(name = "SDT_NHAN")
    private String soDienThoaiNhan;

    @Column(name = "DIA_CHI_NHAN")
    private String diaChiNhan;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "MO_TA")
    private String ghiChu;

}

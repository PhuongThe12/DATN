package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_HOA_DON_GOC")
    @JsonBackReference
    private HoaDon hoaDonGoc;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG")
    @JsonBackReference
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "ID_NHAN_VIEN")
    @JsonBackReference
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ID_KHUYEN_MAI_THEO_DIEU_KIEN")
    private DieuKien dieuKien;

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

    @Column(name = "LOAI_HOA_DON")
    private Integer loaiHoaDon;

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

    @Column(name = "TIEN_GIAM")
    private BigDecimal tienGiam;

    @Column(name = "UU_DAI")
    private Integer uuDai;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<HoaDonChiTiet> listHoaDonChiTiet;

    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<YeuCau> listYeuCau = new ArrayList<>();

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ChiTietThanhToan> chiTietThanhToans;

}

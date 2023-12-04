package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 10)
    @NotNull
    @Column(name = "MA_GIAM_GIA", nullable = false, length = 10)
    private String maGiamGia;

    @NotNull
    @Column(name = "PHAN_TRAM_GIAM", nullable = false)
    private Integer phanTramGiam;

    @Column(name = "SO_LUONG_PHIEU")
    private Integer soLuongPhieu;

    @Column(name = "NGAY_BAT_DAU")
    private LocalDateTime ngayBatDau;

    @Column(name = "NGAY_KET_THUC")
    private LocalDateTime ngayKetThuc;

    @Column(name = "GIA_TRI_DON_TOI_THIEU", precision = 18)
    private BigDecimal giaTriDonToiThieu;

    @Column(name = "GIA_TRI_GIAM_TOI_DA", precision = 18)
    private BigDecimal giaTriGiamToiDa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOI_TUONG_AP_DUNG")
    private HangKhachHang doiTuongApDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NGUOI_TAO")
    private NhanVien nguoiTao;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "SO_LUONG_GIAY_TOI_THIEU")
    private Integer soLuongGiayToiThieu;

}
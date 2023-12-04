package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
public class PhieuGiamGiaChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HOA_DON")
    private HoaDon idHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PHIEU_GIAM_GIA")
    private PhieuGiamGia idPhieuGiamGia;

    @Column(name = "GIA_TRUOC_GIAM", precision = 18)
    private BigDecimal giaTruocGiam;

    @Column(name = "GIA_SAU_GIAM", precision = 18)
    private BigDecimal giaSauGiam;

    @Column(name = "NGAY_TAO")
    private Instant ngayTao;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

}
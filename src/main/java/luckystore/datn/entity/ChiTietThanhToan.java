package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ChiTietThanhToan")
public class ChiTietThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HOA_DON")
    @JsonBackReference
    private HoaDon hoaDon;

    @Column(name = "HINH_THUC_THANH_TOAN", nullable = false)
    private Integer hinhThucThanhToan;

    @Column(name = "TIEN_THANH_TOAN", nullable = false)
    private BigDecimal tienThanhToan;

    @Column(name = "TRANG_THAI", nullable = false)
    private Integer trangThai;

    @Column(name = "GHI_CHU")
    private String ghiChu;

}
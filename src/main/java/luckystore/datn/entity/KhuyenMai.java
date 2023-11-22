package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KhuyenMai")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMai {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEN", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String ten;

    @Column(name = "NGAY_BAT_DAU", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime ngayBatDau;

    @Column(name = "NGAY_KET_THUC", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime ngayKetThuc;

    @Column(name = "TRANG_THAI", columnDefinition = "INT", nullable = false)
    private Integer trangThai;

    @Column(name = "GHI_CHU", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String ghiChu;

    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<KhuyenMaiChiTiet> khuyenMaiChiTiets;

}

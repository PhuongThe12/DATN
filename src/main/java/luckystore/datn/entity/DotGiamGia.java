package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DotGiamGia")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DotGiamGia {

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

    @OneToMany(mappedBy = "dotGiamGia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DieuKien> danhSachDieuKien;

}
package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Table(name = "HangKhachHang")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HangKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TEN_HANG", length = 50)
    private String tenHang;

    @Column(name = "DIEU_KIEN")
    private Integer dieuKien;

    @Column(name = "UU_DAI")
    private Integer uuDai;

    @Nationalized
    @Lob
    @Column(name = "MO_TA")
    private String moTa;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

}
package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "KichThuoc")
public class KichThuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TEN", length = 100)
    private String ten;

    @Column(name = "CHIEU_DAI")
    private Double chieuDai;

    @Column(name = "CHIEU_RONG")
    private Double chieuRong;

    @Size(max = 300)
    @Nationalized
    @Column(name = "MO_TA", length = 300)
    private String moTa;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

<<<<<<< HEAD
//    @Column(name = "NGAY_TAO")
//    private LocalDateTime ngayTao;

=======
>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec
}
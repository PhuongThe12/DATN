package luckystore.datn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
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
    private Instant ngayTao;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

}
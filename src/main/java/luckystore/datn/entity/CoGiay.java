package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CoGiay")
public class CoGiay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TEN", length = 100)
    private String ten;

    @Size(max = 3000)
    @Column(name = "MO_TA", length = 3000)
    private String moTa;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

}
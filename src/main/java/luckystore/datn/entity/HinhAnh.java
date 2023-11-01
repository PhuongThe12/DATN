package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "HinhAnh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Lob
    @Column(name = "LINK")
    private String link;

    @Column(name = "UU_TIEN")
    private Integer uuTien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GIAY")
    private Giay giay;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

}
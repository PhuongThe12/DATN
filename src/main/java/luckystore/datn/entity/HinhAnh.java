package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Giay giay;
<<<<<<< HEAD
//
//    @Column(name = "NGAY_TAO")
//    private LocalDateTime ngayTao;
=======
>>>>>>> a802cd7cc0944af972d7a1888889f2a8d3b6b198

}
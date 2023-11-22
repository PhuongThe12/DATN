package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AnhGiayTra")
public class AnhGiayTra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Nationalized
    @Lob
    @Column(name = "LINK")
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_YEU_CAU_CHI_TIET")
    @JsonBackReference
    private YeuCauChiTiet yeuCauChiTiet;

}

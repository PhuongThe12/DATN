package luckystore.datn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import luckystore.datn.model.request.LyDoRequest;
import org.hibernate.annotations.Nationalized;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LyDo")
public class LyDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TEN", length = 100)
    private String ten;

    @Column(name = "TRANG_THAI", length = 100)
    private Integer trangThai;

    public LyDo(LyDoRequest lyDoRequest) {
        this.id = lyDoRequest.getId();
        this.ten = lyDoRequest.getTen();
        this.trangThai = lyDoRequest.getTrangThai();
    }
}

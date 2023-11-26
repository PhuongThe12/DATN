package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KhuyenMaiChiTiet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiChiTiet {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "KHUYEN_MAI_ID")
    @JsonBackReference
    private KhuyenMai khuyenMai;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BIEN_THE_GIAY_ID")
    @JsonBackReference
    private BienTheGiay bienTheGiay;

    @Column(name = "PHAN_TRAM_GIAM",columnDefinition = "INT", nullable = false)
    private Integer phanTramGiam;

}

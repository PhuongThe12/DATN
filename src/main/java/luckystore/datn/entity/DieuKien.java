package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DieuKien")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DieuKien {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PHAN_TRAM_GIAM",columnDefinition = "INT", nullable = false)
    private Integer phanTramGiam;

    @Column(name = "TONG_HOA_DON", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal tongHoaDon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    private DotGiamGia dotGiamGia;

}


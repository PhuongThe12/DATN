package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "PHAN_TRAM_GIAM",columnDefinition = "INT")
    private Integer phanTramGiam;

    @Column(name = "TONG_HOA_DON", nullable = false)
    private BigDecimal tongHoaDon;

    @ManyToOne
    @JoinColumn(name = "DOT_GIAM_GIA_ID")
    @JsonBackReference
    private DotGiamGia dotGiamGia;

}


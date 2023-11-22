package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "GioHangChiTiet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GioHangChiTiet {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "ID_GIO_HANG")
    @JsonBackReference
    private GioHang gioHang;

    @ManyToOne
    @JoinColumn(name = "ID_BIEN_THE_GIAY")
    @JsonBackReference
    private BienTheGiay bienTheGiay;

    @Column(name = "SO_LUONG_SAN_PHAM")
    private Integer soLuong;

    @Column(name = "GIA_BAN")
    private BigDecimal giaBan;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

    @Column(name = "GHI_CHU")
    private String ghiChu;

}

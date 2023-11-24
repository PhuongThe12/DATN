package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_HOA_DON")
    @JsonBackReference
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "ID_BIEN_THE_GIAY")
    private BienTheGiay bienTheGiay;

    @Column(name = "DON_GIA", precision = 19, scale = 4)
    private BigDecimal donGia;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "SO_LUONG_TRA")
    private Integer soLuongTra;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Lob
    @Column(name = "GHI_CHU")
    private String ghiChu;


}
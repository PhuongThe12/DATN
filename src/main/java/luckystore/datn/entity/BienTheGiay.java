package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "BienTheGiay")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienTheGiay {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "SO_LUONG_LOI")
    private Integer soLuongLoi;

    @Column(name = "HINH_ANH")
    private String hinhAnh;

    @Column(name = "GIA_NHAP")
    private BigDecimal giaNhap;

    @Column(name = "GIA_BAN")
    private BigDecimal giaBan;

    @Column(name = "BAR_CODE")
    private String barCode;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "ID_GIAY")
    @JsonBackReference
    private Giay giay;

    @ManyToOne
    @JoinColumn(name = "ID_MAU_SAC")
    private MauSac mauSac;

    @ManyToOne
    @JoinColumn(name = "ID_KICH_THUOC")
    private KichThuoc kichthuoc;

}

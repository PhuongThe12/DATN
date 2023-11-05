package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "YeuCauChiTiet")
public class YeuCauChiTiet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_YEU_CAU")
    @JsonBackReference
    private YeuCau yeuCau;

    @OneToOne
    @JoinColumn(name = "ID_HOA_DON_CHI_TIET")
    private HoaDonChiTiet idHoaDonChiTiet;

    @Column(name = "ID_BIEN_THE_GIAY")
    private Long idSanPhamChiTiet;

    @Column(name = "LY_DO")
    private String lyDo;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "Hinh_Anh")
    private String hinhAnh;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "GHI_CHU")
    private String ghiChu;
}

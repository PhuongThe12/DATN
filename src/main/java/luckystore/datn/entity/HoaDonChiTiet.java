package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_HOA_DON")
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_BIEN_THE_GIAY")
    private BienTheGiay bienTheGiay;

    @Column(name = "DON_GIA")
    private Long donGia;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "GHI_CHU")
    private String ghiChu;

}

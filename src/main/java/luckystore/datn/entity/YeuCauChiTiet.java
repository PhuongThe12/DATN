package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "YeuCauChiTiet")
public class YeuCauChiTiet {
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
    private HoaDonChiTiet hoaDonChiTiet;

    @OneToOne
    @JoinColumn(name = "ID_BIEN_THE_GIAY")
    private BienTheGiay bienTheGiay;

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

    public YeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest) {
        if (yeuCauChiTietRequest != null) {
            this.id = yeuCauChiTietRequest.getId();
//            this.yeuCau = yeuCauChiTietRequest.getYeuCau();
//            this.hoaDonChiTiet = yeuCauChiTietRequest.getHoaDonChiTiet();
//            this.bienTheGiay = yeuCauChiTietRequest.getBienTheGiay();
//            this.lyDo = yeuCauChiTietRequest.getLyDo();
//            this.soLuong = yeuCauChiTietRequest.getSoLuong();
//            this.trangThai = yeuCauChiTietRequest.getTrangThai();
//            this.ghiChu = yeuCauChiTietRequest.getGhiChu();
        }
    }

    public YeuCauChiTiet(YeuCau yeuCau, HoaDonChiTiet hoaDonChiTiet, BienTheGiay bienTheGiay, String lyDo, Integer soLuong, Integer trangThai, String ghiChu) {
        this.yeuCau = yeuCau;
        this.hoaDonChiTiet = hoaDonChiTiet;
        this.bienTheGiay = bienTheGiay;
        this.lyDo = lyDo;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }
}

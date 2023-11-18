package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import org.hibernate.annotations.Nationalized;

import java.util.List;


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

    @OneToOne
    @JoinColumn(name = "LY_DO")
    private LyDo lyDo;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "GHI_CHU")
    @Nationalized
    private String ghiChu;

    @OneToMany(mappedBy = "yeuCauChiTiet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AnhGiayTra> listAnhGiayTra;

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

}

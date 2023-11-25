package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import org.hibernate.annotations.Nationalized;



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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_YEU_CAU")
    @JsonBackReference
    private YeuCau yeuCau;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_HOA_DON_CHI_TIET")
    @JsonBackReference
    private HoaDonChiTiet hoaDonChiTiet;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID_BIEN_THE_GIAY")
    @JsonBackReference
    private BienTheGiay bienTheGiay;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LY_DO")
    private LyDo lyDo;

    @Column(name = "SO_LUONG")
    private Integer soLuong;

    @Column(name = "TRANG_THAI") // 0 : "Chờ xét duyệt" / 1 : "Đã xác nhận" / 2 : "Đã Hủy" / 3 : "Chờ xét duyệt - Hủy đổi" / 4 : "Đã xác nhận - Hủy đổi"
    private Integer trangThai;

    @Column(name = "LOAI_YEU_CAU_CHI_TIET")
    private Integer loaiYeuCauChiTiet;

    @Column(name = "TINH_TRANG_SAN_PHAM")
    private Boolean tinhTrangSanPham;
    @Column(name = "GHI_CHU")
    @Nationalized
    private String ghiChu;

//    @OneToMany(mappedBy = "yeuCauChiTiet", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<AnhGiayTra> listAnhGiayTra;

    public YeuCauChiTiet(YeuCauChiTietRequest yeuCauChiTietRequest,HoaDonChiTiet hoaDonChiTiet,BienTheGiay bienTheGiay,LyDo lyDo,YeuCau yeuCau) {
        if (yeuCauChiTietRequest != null) {
            this.yeuCau = yeuCau;
            this.hoaDonChiTiet = hoaDonChiTiet;
            this.bienTheGiay = bienTheGiay;
            this.lyDo = lyDo;
            this.soLuong = yeuCauChiTietRequest.getSoLuong();
            this.trangThai = yeuCauChiTietRequest.getTrangThai();
            this.tinhTrangSanPham = yeuCauChiTietRequest.getTinhTrangSanPham();
            this.loaiYeuCauChiTiet = yeuCauChiTietRequest.getLoaiYeuCauChiTiet();
            this.ghiChu = yeuCauChiTietRequest.getGhiChu();
        }
    }


}

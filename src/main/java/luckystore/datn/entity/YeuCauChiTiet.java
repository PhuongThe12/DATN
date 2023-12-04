package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;


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
    @JoinColumn(name = "ID_LY_DO")
    private LyDo lyDo;

    @Column(name = "TIEN_GIAM")
    private BigDecimal tienGiam;
    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;

    @Column(name = "TRANG_THAI") //      //1: chờ xác nhận - chờ xác nhân   2:chờ xác nhận - hủy   3:đã xác nhận - hủy  4: đã xác nhận - đã xác nhận   5: Đã hủy - Đã hủy
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
            this.thanhTien = yeuCauChiTietRequest.getThanhTien();
            this.tienGiam = yeuCauChiTietRequest.getTienGiam();
            this.trangThai = yeuCauChiTietRequest.getTrangThai();
            this.tinhTrangSanPham = yeuCauChiTietRequest.getTinhTrangSanPham();
            this.loaiYeuCauChiTiet = yeuCauChiTietRequest.getLoaiYeuCauChiTiet();
            this.ghiChu = yeuCauChiTietRequest.getGhiChu();
        }
    }


    public YeuCauChiTiet(YeuCau yeuCau,HoaDonChiTiet hoaDonChiTiet, BienTheGiay bienTheGiay, LyDo lyDo, BigDecimal tienGiam, BigDecimal thanhTien, Integer trangThai, Integer loaiYeuCauChiTiet, Boolean tinhTrangSanPham, String ghiChu) {
        this.yeuCau = yeuCau;
        this.hoaDonChiTiet = hoaDonChiTiet;
        this.bienTheGiay = bienTheGiay;
        this.lyDo = lyDo;
        this.tienGiam = tienGiam;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.loaiYeuCauChiTiet = loaiYeuCauChiTiet;
        this.tinhTrangSanPham = tinhTrangSanPham;
        this.ghiChu = ghiChu;
    }
}
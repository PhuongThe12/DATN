package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "YeuCau")
public class YeuCau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "ID_NGUOI_THUC_HIEN")
    private Long nguoiThucHien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HOA_DON")
    @JsonBackReference
    private HoaDon hoaDon;

    @Column(name = "TIEN_KHACH_TRA")
    private BigDecimal tienKhachTra;
    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "THONG_TIN_NHAN_HANG")
    private String thongTinNhanHang;

    @Column(name = "PHI_SHIP")
    private BigDecimal phiShip;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;
    @Column(name = "NGAY_SUA")
    private LocalDateTime ngaySua;

    @Column(name = "NGUOI_TAO")
    private Long nguoiTao;

    @Column(name = "NGUOI_SUA")
    private Long nguoiSua;
    @Column(name = "GHI_CHU")
    private String ghiChu;

    @OneToMany(mappedBy = "yeuCau",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<YeuCauChiTiet> listYeuCauChiTiet;

    public YeuCau(YeuCauRequest yeuCauRequest,HoaDon hoaDon,Long hoaDonDoiTra,LocalDateTime ngayTao,LocalDateTime ngaySua) {
        if (yeuCauRequest != null) {
            this.hoaDon = hoaDon;
            this.trangThai = yeuCauRequest.getTrangThai();
            this.ngayTao = ngayTao;
            this.ngaySua = ngaySua;
            this.ghiChu = yeuCauRequest.getGhiChu();
            this.thongTinNhanHang = yeuCauRequest.getThongTinNhanHang();
            this.phiShip = yeuCauRequest.getPhiShip();
            this.tienKhachTra = yeuCauRequest.getTienKhachTra();
        }
    }

}


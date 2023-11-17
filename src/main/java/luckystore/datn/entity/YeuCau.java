package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauRequest;

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
    private HoaDon hoaDon;

    @Column(name = "LOAI_YEU_CAU")
    private Integer loaiYeuCau;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "NGAY_TAO")
    private Date ngayTao;

    @Column(name = "NGAY_SUA")
    private Date ngaySua;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @OneToMany(mappedBy = "yeuCau",fetch = FetchType.LAZY)
    @Column(nullable = true)
    @JsonManagedReference
    private Set<YeuCauChiTiet> listYeuCauChiTiet;

    public YeuCau(YeuCauRequest YeuCauRequest,HoaDon hoaDon) {
        if (YeuCauRequest != null) {
            this.nguoiThucHien = YeuCauRequest.getNguoiThucHien();
            this.hoaDon = hoaDon;
            this.loaiYeuCau = YeuCauRequest.getLoaiYeuCau();
            this.trangThai = YeuCauRequest.getTrangThai();
            this.ghiChu = YeuCauRequest.getGhiChu();
        }
    }
}


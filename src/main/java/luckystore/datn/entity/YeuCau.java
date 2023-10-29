package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import luckystore.datn.model.request.YeuCauRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "YeuCau")
public class YeuCau implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_NGUOI_THUC_HIEN")
    private Long nguoiThucHien;

    @ManyToOne
    @JoinColumn(name = "ID_HOA_DON")
    private HoaDon hoaDon;

    @Column(name = "Loai_Yeu_Cau")
    private Integer loaiYeuCau;

    @Column(name = "Trang_Thai")
    private Integer trangThai;

    @Column(name = "Ngay_Tao")
    private Date ngayTao;

    @Column(name = "Ngay_Sua")
    private Date ngaySua;

    @Column(name = "Ghi_Chu")
    private String ghiChu;

    @OneToMany(mappedBy = "yeuCau",fetch = FetchType.LAZY)
    @Column(nullable = true)
    @JsonManagedReference
    private Set<YeuCauChiTiet> yeuCauChiTiets;

    public YeuCau(YeuCauRequest YeuCauRequest) {
        if (YeuCauRequest != null) {
            this.nguoiThucHien = YeuCauRequest.getNguoiThucHien();
            this.hoaDon = YeuCauRequest.getHoaDon();
            this.loaiYeuCau = YeuCauRequest.getLoaiYeuCau();
            this.trangThai = YeuCauRequest.getTrangThai();
            this.ngayTao = YeuCauRequest.getNgayTao();
            this.ngaySua = YeuCauRequest.getNgaySua();
            this.ghiChu = YeuCauRequest.getGhiChu();
        }
    }
}


package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "GioHang")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GioHang {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG")
    private KhachHang khachHang;

    @Column(name = "NGAY_TAO")
    private LocalDateTime ngayTao;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @OneToMany(mappedBy = "gioHang",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GioHangChiTiet> gioHangChiTiets;

}

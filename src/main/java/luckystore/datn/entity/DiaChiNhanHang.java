package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "DiaChiNhanHang")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaChiNhanHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Lob
    @Column(name = "DIA_CHI_NHAN")
    private String diaChiNhan;

    @Column(name = "NGAY_TAO")
    private Date ngayTao;

    @Column(name = "SO_DIEN_THOAI_NHAN", length = 20)
    private String soDienThoaiNhan;

    @Column(name = "HO_TEN", length = 100)
    private String hoTen;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "ID_KHACH_HANG")
    private KhachHang idKhachHang;


}
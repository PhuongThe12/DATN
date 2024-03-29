package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;


@Entity
@Table(name = "KhachHang")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "HO_TEN", length = 100)
    private String hoTen;

    @Column(name = "GIOI_TINH")
    private Boolean gioiTinh;

    @Column(name = "NGAY_SINH")
    private Date ngaySinh;

    @Column(name = "SO_DIEN_THOAI", length = 20)
    private String soDienThoai;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "DIEM_TICH_LUY")
    private Integer diemTichLuy;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_HANG_KHACH_HANG")
    private HangKhachHang hangKhachHang;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_TAI_KHOAN")
    private TaiKhoan taiKhoan;

}
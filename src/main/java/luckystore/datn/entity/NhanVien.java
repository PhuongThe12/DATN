package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HO_TEN")
    private String hoTen;

    @Column(name = "GIOI_TINH")
    private Integer gioiTinh;

    @Column(name = "NGAY_SINH")
    private Date ngaySinh;

    @Column(name = "SDT")
    private String soDienThoai;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "XA")
    private String xa;

    @Column(name = "HUYEN")
    private String huyen;

    @Column(name = "TINH")
    private String tinh;

    @Column(name = "CHUC_VU")
    private Integer chucVu;

    @ManyToOne
    @JoinColumn(name = "ID_TAI_KHOAN")
    private TaiKhoan taiKhoan;

}

package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TaiKhoan")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoan {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEN_DANG_NHAP")
    private String tenDangNhap;

    @Column(name = "MAT_KHAU")
    private String matKhau;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "ROLE")
    private Integer role;
}

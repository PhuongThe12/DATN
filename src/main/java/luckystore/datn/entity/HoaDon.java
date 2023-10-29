package luckystore.datn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_HOA_DON_GOC")
    private Long idHoaDonGoc;

    @Column(name = "ID_KHACH_HANG")
    private String idKhachHang;

    @Column(name = "TONG_TIEN")
    private Long tongTien;

    @Column(name = "NGAY_TAO")
    private Date ngayTao;

    @Column(name = "KENH_BAN")
    private Integer kenhBan;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

}

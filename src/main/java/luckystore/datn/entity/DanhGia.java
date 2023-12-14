package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "DanhGia")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanhGia {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SAO_DANH_GIA", columnDefinition = "INT", nullable = true)
    private Integer saoDanhGia;

    @Column(name = "BINH_LUAN", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    private String binhLuan;

    @Column(name = "TRANG_THAI", columnDefinition = "INT", nullable = true)
    private Integer trangThai;

    @Column(name = "THOI_GIAN", columnDefinition = "DATETIME", nullable = true)
    private Date thoiGian;

    @Column(name = "NGAY_TAO", columnDefinition = "DATETIME", nullable = true)
    private Date ngayTao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_KHACH_HANG")
    @JsonBackReference
    private KhachHang khachHang;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_GIAY")
    @JsonBackReference
    private Giay giay;

}

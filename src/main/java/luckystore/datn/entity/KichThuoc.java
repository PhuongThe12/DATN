package luckystore.datn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KichThuoc")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KichThuoc {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEN")
    private String ten;

    @Column(name = "CHIEU_DAI")
    private Float chieuDai;

    @Column(name = "CHIEU_RONG")
    private Float chieuRong;

    @Column(name = "MO_TA")
    private String moTa;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

}

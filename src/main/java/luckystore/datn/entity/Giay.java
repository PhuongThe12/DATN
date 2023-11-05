package luckystore.datn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Giay")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Giay {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEN")
    private String ten;

    @Column(name = "NAM_SX")
    private Integer namSX;

    @Column(name = "TRANG_THAI")
    private Integer trangThai;

    @Column(name = "MO_TA")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "ID_DE_GIAY")
    private DeGiay deGiay;

    @ManyToOne
    @JoinColumn(name = "ID_THUONG_HIEU")
    private ThuongHieu thuongHieu;

    @ManyToOne
    @JoinColumn(name = "ID_CO_GIAY")
    private CoGiay coGiay;

    @ManyToOne
    @JoinColumn(name = "ID_LOT_GIAY")
    private LotGiay lotGiay;

    @ManyToOne
    @JoinColumn(name = "ID_MUI_GIAY")
    private MuiGiay muiGiay;

    @ManyToOne
    @JoinColumn(name = "ID_CHAT_LIEU")
    private ChatLieu chatLieu;

    @ManyToOne
    @JoinColumn(name = "ID_DAY_GIAY")
    private DayGiay dayGiay;

    @OneToMany(mappedBy = "giay")
    @JsonManagedReference
    private List<BienTheGiay> lstBienTheGiay;

    @OneToMany(mappedBy = "giay")
    @JsonManagedReference
    private List<HinhAnh> lstAnh;

}

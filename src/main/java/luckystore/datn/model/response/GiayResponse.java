package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HinhAnh;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiayResponse {

    private Long id;

    private String ten;

    private Integer namSX;

    private List<String> lstAnh;

    private Integer trangThai;

    private String moTa;

    private DeGiayResponse deGiay;

    private ThuongHieuResponse thuongHieu;

    private CoGiayResponse coGiay;

    private LotGiayResponse lotGiay;

    private MuiGiayResponse muiGiay;

    private ChatLieuResponse chatLieu;

    private DayGiayResponse dayGiay;

    private List<BienTheGiayResponse> lstBienTheGiay;

    public GiayResponse(Giay giay) {
        if (giay != null) {
            this.id = giay.getId();
            this.ten = giay.getTen();
            this.namSX = giay.getNamSX();
            this.lstAnh = giay.getLstAnh().stream().map(HinhAnh::getLink).collect(Collectors.toList());
            this.trangThai = giay.getTrangThai();
            this.moTa = giay.getMoTa();
            this.deGiay = new DeGiayResponse(giay.getDeGiay());
            this.thuongHieu = new ThuongHieuResponse(giay.getThuongHieu());
            this.coGiay = new CoGiayResponse(giay.getCoGiay());
            this.lotGiay = new LotGiayResponse(giay.getLotGiay());
            this.muiGiay = new MuiGiayResponse(giay.getMuiGiay());
            this.chatLieu = new ChatLieuResponse(giay.getChatLieu());
            this.dayGiay = new DayGiayResponse(giay.getDayGiay());

            this.lstBienTheGiay = (giay.getLstBienTheGiay().stream().map(BienTheGiayResponse::new).collect(Collectors.toList()));

        }
    }

    public GiayResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
//        this.lstAnh = lstAnh.stream().map(HinhAnh::getLink).collect(Collectors.toList());
    }

}

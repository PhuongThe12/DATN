package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HashTagChiTiet;
import luckystore.datn.entity.HinhAnh;
import luckystore.datn.service.impl.ImageHubServiceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiayResponse {

    private Long id;

    private String ten;

    private Integer namSX;

    private List<String> lstAnh = new ArrayList<>();

    private Integer trangThai;

    private String moTa;

    private DeGiayResponse deGiay;

    private ThuongHieuResponse thuongHieu;

    private CoGiayResponse coGiay;

    private LotGiayResponse lotGiay;

    private MuiGiayResponse muiGiay;

    private ChatLieuResponse chatLieu;

    private DayGiayResponse dayGiay;

    private List<BienTheGiayResponse> lstBienTheGiay = new ArrayList<>();

    private Map<Long, String> mauSacImages;

    private List<HashTagChiTietResponse> lstHashTagChiTiet;

    public GiayResponse(Giay giay) {
        if (giay != null) {
            this.id = giay.getId();
            this.ten = giay.getTen();
            this.namSX = giay.getNamSX();
            this.lstAnh = giay.getLstAnh().stream().sorted(Comparator.comparingInt(HinhAnh::getUuTien))
                    .map(anh -> ImageHubServiceImpl.getBase64FromFileStatic(anh.getLink())).collect(Collectors.toList());
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
            this.lstHashTagChiTiet = (giay.getHashTagChiTiets().stream().map(HashTagChiTietResponse::new).collect(Collectors.toList()));

        }
    }

    public GiayResponse(Long id, String ten, String thubmail) {
        this.id = id;
        this.ten = ten;
        lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(thubmail));
    }

    public GiayResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

}

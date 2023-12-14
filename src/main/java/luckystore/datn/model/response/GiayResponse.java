package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.HinhAnh;
import luckystore.datn.service.impl.ImageHubServiceImpl;

import java.math.BigDecimal;
import java.util.*;
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

    private HinhAnh hinhAnh;

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

    private BigDecimal giaTu;

    private BigDecimal giaDen;

    private Long soLuongThongKe;

    private Integer phanTramGiam = 0;

    public GiayResponse(Long id) {
        this.id = id;
    }

    public GiayResponse(Long id, Long soLuongThongKe) {
        this.id = id;
//        System.out.println("soLuong: " + soLuongThongKe);
//        this.soLuongThongKe = soLuongThongKe;
    }

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

    public GiayResponse(Long id, String ten, List<BienTheGiay> lstBienTheGiay) {
        this.id = id;
        this.ten = ten;
//        this.lstAnh = lstAnh.stream().sorted(Comparator.comparingInt(HinhAnh::getUuTien))
//                .map(anh -> ImageHubServiceImpl.getBase64FromFileStatic(anh.getLink())).collect(Collectors.toList());
        this.lstBienTheGiay = lstBienTheGiay.stream().map(BienTheGiayResponse::new).collect(Collectors.toList());
    }

    public GiayResponse(Long id, String ten, BienTheGiay bienThe, String thumbnail) {
        this.id = id;
        this.ten = ten;
        if (thumbnail != null) {
            this.lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(thumbnail));
        }
        this.lstBienTheGiay.add(new BienTheGiayResponse(bienThe));
    }

    public GiayResponse(Long id, String ten, String thumbnail) {
        this.id = id;
        this.ten = ten;
        lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(thumbnail));
    }

    public GiayResponse(Long id, String ten, String thumbnail, BienTheGiay bienTheGiay) {
        this.id = id;
        this.ten = ten;
        this.lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(thumbnail));
        this.lstBienTheGiay.add(new BienTheGiayResponse(bienTheGiay));
    }

    public GiayResponse(Long id, String ten, String thumbnail, BigDecimal giaTu, BigDecimal giaDen) {
        this.id = id;
        this.ten = ten;
        if (thumbnail != null) {
            lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(thumbnail));
        }
        this.giaTu = giaTu;
        this.giaDen = giaDen;
    }

    public GiayResponse(Long id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public GiayResponse(Long id, String ten, String tenThuongHieu, String linkAnh, Long idBienThe, String tenMau, String tenKichThuoc, BigDecimal giaBan) {
        this.id = id;
        this.ten = ten;
        this.thuongHieu = ThuongHieuResponse.builder().ten(tenThuongHieu).build();
        BienTheGiayResponse bienTheGiayResponse = new BienTheGiayResponse();
        bienTheGiayResponse.setId(idBienThe);
        bienTheGiayResponse.setKichThuoc(KichThuocResponse.builder().ten(tenKichThuoc).build());
        bienTheGiayResponse.setMauSac(MauSacResponse.builder().ten(tenMau).build());
        bienTheGiayResponse.setGiaBan(giaBan);
        this.getLstBienTheGiay().add(bienTheGiayResponse);
        lstAnh.add(ImageHubServiceImpl.getBase64FromFileStatic(linkAnh));
    }

    public GiayResponse(Giay giay, Long soLuongThongKe) {
        this.id = giay.getId();
        this.ten = giay.getTen();
        this.thuongHieu = new ThuongHieuResponse(giay.getThuongHieu().getId(), giay.getThuongHieu().getTen());
        this.lstAnh = giay.getLstAnh().stream().sorted(Comparator.comparingInt(HinhAnh::getUuTien))
                .map(anh -> ImageHubServiceImpl.getBase64FromFileStatic(anh.getLink())).collect(Collectors.toList());
        this.soLuongThongKe = soLuongThongKe;
    }


    public GiayResponse(Giay giay, String ten) {
        this.id = giay.getId();
        this.ten = ten;
        this.thuongHieu = new ThuongHieuResponse(giay.getThuongHieu().getId(), giay.getThuongHieu().getTen());
    }
    public GiayResponse(Long id, Integer phanTramGiam) {
        this.id = id;
        this.phanTramGiam = phanTramGiam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiayResponse that = (GiayResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);

    }
}

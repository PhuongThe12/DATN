package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.service.impl.ImageHubServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietResponse {

    private Long id;
    private YeuCauResponse yeuCau;
    private HoaDonChiTietResponse hoaDonChiTiet;
    private BienTheGiayResponse bienTheGiay;
    private LyDoResponse lyDo;
    private BigDecimal tienGiam;
    private BigDecimal thanhTien;
    private Integer trangThai;
    private Boolean tinhTrangSanPham;
    private Integer loaiYeuCauChiTiet;
    private String ghiChu;

//    private List<String> listAnhGiayTra = new ArrayList<>();

    public YeuCauChiTietResponse(YeuCauChiTiet yeuCauChiTiet) {
        if(yeuCauChiTiet != null){
            this.id = yeuCauChiTiet.getId();
            this.yeuCau = new YeuCauResponse(yeuCauChiTiet.getYeuCau());
            this.hoaDonChiTiet = new HoaDonChiTietResponse(yeuCauChiTiet.getHoaDonChiTiet());
            this.bienTheGiay = new BienTheGiayResponse(yeuCauChiTiet.getBienTheGiay());
            this.lyDo = new LyDoResponse(yeuCauChiTiet.getLyDo());
            this.tienGiam = yeuCauChiTiet.getTienGiam();
            this.thanhTien = yeuCauChiTiet.getThanhTien();
            this.trangThai = yeuCauChiTiet.getTrangThai();
            this.tinhTrangSanPham = yeuCauChiTiet.getTinhTrangSanPham();
            this.loaiYeuCauChiTiet = yeuCauChiTiet.getLoaiYeuCauChiTiet();
            this.ghiChu = yeuCauChiTiet.getGhiChu();
//            this.listAnhGiayTra = yeuCauChiTiet.getListAnhGiayTra().stream().map(anhGiayTra -> ImageHubServiceImpl.getBase64FromFileStatic(anhGiayTra.getLink())).collect(Collectors.toList());
        }
    }

}

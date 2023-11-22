package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.AnhGiayTra;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.YeuCau;
import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.service.impl.ImageHubServiceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauResponse {

    private Long id;
    private Long nguoiThucHien;
    private Long hoaDon;
    private Integer trangThai;
    private Date ngayTao;
    private Date ngaySua;
    private String ghiChu;

    private List<String> listAnhGiayTra = new ArrayList<>();

    private List<YeuCauChiTietResponse> listYeuCauChiTiet = new ArrayList<>();

    public YeuCauResponse(YeuCau yeuCau){
        if(yeuCau != null){
            this.id = yeuCau.getId();
            this.nguoiThucHien = yeuCau.getNguoiThucHien();
            this.hoaDon = yeuCau.getHoaDon().getId();
            this.trangThai = yeuCau.getTrangThai();
            this.ngayTao = yeuCau.getNgayTao();
            this.ngaySua = yeuCau.getNgaySua();
            this.ghiChu = yeuCau.getGhiChu();
        }
    }

    public YeuCauResponse(YeuCauResponse responseByStatus) {
        if(responseByStatus != null){
            this.id = responseByStatus.getId();
            this.nguoiThucHien = responseByStatus.getNguoiThucHien();
            this.hoaDon = responseByStatus.getHoaDon();
            this.trangThai = responseByStatus.getTrangThai();
            this.ngayTao = responseByStatus.getNgayTao();
            this.ngaySua = responseByStatus.getNgaySua();
            this.ghiChu = responseByStatus.getGhiChu();
            this.listYeuCauChiTiet = responseByStatus.getListYeuCauChiTiet();
        }
    }
}

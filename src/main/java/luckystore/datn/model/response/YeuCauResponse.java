package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.YeuCau;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauResponse {

    private Long id;
    private Long nguoiThucHien;
    private Long hoaDon;
    private Integer loaiYeuCau;
    private Integer trangThai;
    private Date ngayTao;
    private Date ngaySua;
    private String ghiChu;

    public YeuCauResponse(YeuCau yeuCau){
        if(yeuCau != null){
            this.id = yeuCau.getId();
            this.nguoiThucHien = yeuCau.getNguoiThucHien();
            this.hoaDon = yeuCau.getHoaDon().getId();
            this.loaiYeuCau = yeuCau.getLoaiYeuCau();
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
            this.loaiYeuCau = responseByStatus.getLoaiYeuCau();
            this.trangThai = responseByStatus.getTrangThai();
            this.ngayTao = responseByStatus.getNgayTao();
            this.ngaySua = responseByStatus.getNgaySua();
            this.ghiChu = responseByStatus.getGhiChu();
        }
    }
}

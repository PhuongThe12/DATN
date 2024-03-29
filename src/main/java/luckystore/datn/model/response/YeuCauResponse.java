package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import luckystore.datn.entity.YeuCau;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauResponse {

    private Long id;
    private Long nguoiThucHien;
    private HoaDonYeuCauRespone hoaDon;
    private Integer trangThai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngaySua;
    private String thongTinNhanHang;
    private BigDecimal phiShip;
    private Long nguoiTao;
    private Long nguoiSua;
    private String ghiChu;

    private List<String> listAnhGiayTra = new ArrayList<>();

    private List<YeuCauChiTietResponse> listYeuCauChiTiet = new ArrayList<>();

    public YeuCauResponse(YeuCau yeuCau){
        if(yeuCau != null){
            this.id = yeuCau.getId();
            this.nguoiThucHien = yeuCau.getNguoiThucHien();
            this.hoaDon = new HoaDonYeuCauRespone(yeuCau.getHoaDon(),"getAllYeuCau");
            this.trangThai = yeuCau.getTrangThai();
            this.thongTinNhanHang = yeuCau.getThongTinNhanHang();
            this.phiShip = yeuCau.getPhiShip();
            this.ngayTao = yeuCau.getNgayTao();
            this.ngaySua = yeuCau.getNgaySua();
            this.nguoiTao = yeuCau.getNguoiTao();
            this.nguoiSua = yeuCau.getNguoiSua();
            this.ghiChu = yeuCau.getGhiChu();
        }
    }

    public YeuCauResponse(YeuCau yeuCau,String getListYeuCauKhachHang){
        if(yeuCau != null){
            this.id = yeuCau.getId();
            this.nguoiThucHien = yeuCau.getNguoiThucHien();
            this.trangThai = yeuCau.getTrangThai();
            this.thongTinNhanHang = yeuCau.getThongTinNhanHang();
            this.phiShip = yeuCau.getPhiShip();
            this.ngayTao = yeuCau.getNgayTao();
            this.ngaySua = yeuCau.getNgaySua();
            this.nguoiTao = yeuCau.getNguoiTao();
            this.nguoiSua = yeuCau.getNguoiSua();
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

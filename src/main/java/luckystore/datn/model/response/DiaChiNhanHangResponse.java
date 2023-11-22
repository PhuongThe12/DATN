package luckystore.datn.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.entity.Districts;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.Provinces;
import luckystore.datn.entity.Wards;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaChiNhanHangResponse {
    private Long id;

    @NotNull(message = "Không được để trống địa chỉ nhận")
    private String diaChiNhan;

    private Date ngayTao;

    private String soDienThoaiNhan;

    private String hoTen;

    private Integer trangThai;

    private KhachHang khachHang;

    private String provinces;

    private String districts;

    private String wards;

    public DiaChiNhanHangResponse(DiaChiNhanHang diaChiNhanHang) {
        if(diaChiNhanHang != null){
            this.id = diaChiNhanHang.getId();
            this.diaChiNhan = diaChiNhanHang.getDiaChiNhan();
            this.soDienThoaiNhan= diaChiNhanHang.getSoDienThoaiNhan();
            this.hoTen = diaChiNhanHang.getHoTen();
            this.trangThai =diaChiNhanHang.getTrangThai();
            this.khachHang=diaChiNhanHang.getIdKhachHang();
            this.provinces = diaChiNhanHang.getProvince();
            this.districts = diaChiNhanHang.getDistrict();
            this.wards = diaChiNhanHang.getWard();
        }
    }
}

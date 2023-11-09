package luckystore.datn.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.entity.KhachHang;

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

    private KhachHang idKhachHang;

    public DiaChiNhanHangResponse(DiaChiNhanHang diaChiNhanHang) {
        if(diaChiNhanHang != null){
            this.id = diaChiNhanHang.getId();
            this.diaChiNhan = diaChiNhanHang.getDiaChiNhan();
            this.soDienThoaiNhan= diaChiNhanHang.getSoDienThoaiNhan();
            this.hoTen = diaChiNhanHang.getHoTen();
            this.trangThai =diaChiNhanHang.getTrangThai();
            this.idKhachHang=diaChiNhanHang.getIdKhachHang();
        }
    }
}

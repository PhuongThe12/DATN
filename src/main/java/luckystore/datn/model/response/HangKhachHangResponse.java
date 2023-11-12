package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HangKhachHang;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HangKhachHangResponse {
    private Long id;

    private String tenHang;

    private Integer dieuKien;

    private Integer uuDai;

    private String moTa;

    private Integer trangThai;

    public HangKhachHangResponse(HangKhachHang hangKhachHang) {
        if (hangKhachHang != null) {
            this.id = hangKhachHang.getId();
            this.tenHang = hangKhachHang.getTenHang();
            this.dieuKien= hangKhachHang.getDieuKien();
            this.uuDai = hangKhachHang.getUuDai();
            this.moTa = hangKhachHang.getMoTa();
            this.trangThai = hangKhachHang.getTrangThai();
        }
    }
}

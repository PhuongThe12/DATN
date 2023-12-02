package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.SanPhamYeuThich;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SanPhamYeuThichResponse {

    private Long id;

    private Long idKhachHang;

    private Long idGiay;

    private String tenGiay;

    public SanPhamYeuThichResponse(SanPhamYeuThich sanPhamYeuThich) {
        if (sanPhamYeuThich != null) {
            this.id = sanPhamYeuThich.getId();
            this.idGiay=sanPhamYeuThich.getGiay().getId();
            this.idKhachHang = sanPhamYeuThich.getKhachHang().getId();
            this.tenGiay =sanPhamYeuThich.getGiay().getTen();
        }
    }
}

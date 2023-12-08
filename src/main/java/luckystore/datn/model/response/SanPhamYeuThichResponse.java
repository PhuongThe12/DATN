package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.SanPhamYeuThich;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SanPhamYeuThichResponse {

    private Long id;

    private KhachHangResponse khachHangResponse;

    private GiayResponse giayResponse;

    private BigDecimal giaBan;

    private String hinhAnh;

    private Integer soLuong;


    public SanPhamYeuThichResponse(SanPhamYeuThich sanPhamYeuThich) {
        if (sanPhamYeuThich != null) {
            this.id = sanPhamYeuThich.getId();
            if(sanPhamYeuThich.getKhachHang() != null){
                 khachHangResponse = new KhachHangResponse();
                 khachHangResponse.setId(sanPhamYeuThich.getKhachHang().getId());
                 khachHangResponse.setHoTen(sanPhamYeuThich.getKhachHang().getHoTen());
            }
            if(sanPhamYeuThich.getGiay() != null){
                giayResponse = new GiayResponse();
                giayResponse.setId(sanPhamYeuThich.getGiay().getId());
                giayResponse.setTen(sanPhamYeuThich.getGiay().getTen());
                giayResponse.setLstBienTheGiay(null);

            }
            if (sanPhamYeuThich.getGiay() != null && sanPhamYeuThich.getGiay().getLstBienTheGiay() != null
                    && !sanPhamYeuThich.getGiay().getLstBienTheGiay().isEmpty()) {
                BienTheGiay bienTheGiay = sanPhamYeuThich.getGiay().getLstBienTheGiay().get(0);
                this.giaBan = bienTheGiay.getGiaBan();
                this.hinhAnh = bienTheGiay.getHinhAnh();
                this.soLuong = bienTheGiay.getSoLuong();
            }
        }
    }
}

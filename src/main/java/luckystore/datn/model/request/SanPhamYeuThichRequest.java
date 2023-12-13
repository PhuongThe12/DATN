package luckystore.datn.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Giay;
import luckystore.datn.entity.KhachHang;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamYeuThichRequest {

//    private Long id;

    private Long khachHangId;

    private Long giayId;
}

package luckystore.datn.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.KhachHang;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaChiNhanHangRequest {

    private Long id;

//    @NotNull(message = "Không được để trống địa chỉ nhận")
    private String diaChiNhan;

    private Date ngayTao;

    private String soDienThoaiNhan;

    private String hoTen;

    private Integer trangThai;

    private KhachHang idKhachHang;
}

package luckystore.datn.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietRequest {

    private Long id;

    private Long hoaDonChiTiet;

    private Long bienTheGiay;

    private Long lyDo;

    private BigDecimal thanhTien;

    private BigDecimal tienGiam;

    private Long bienTheGiayTra;

    private Integer soLuongTra;

    private Integer loaiYeuCauChiTiet;

    private Integer trangThai;

    private String ghiChu;

    private Boolean tinhTrangSanPham;


//    @NotNull(message = "Không được để trống hình ảnh", groups = {UpdateGroup.class, CreateGroup.class})
//    List<String> listAnhGiayTra = new ArrayList<>();
}


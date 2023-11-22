package luckystore.datn.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGroup;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauChiTietRequest {

    private Long id;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long hoaDonChiTiet;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long bienTheGiay;

    @NotBlank(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long lyDo;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Integer soLuong;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long bienTheGiayTra;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Integer soLuongTra;

    private Integer loaiYeuCauChiTiet;

    private Integer trangThai;

    private String ghiChu;

    private int sanPhamLoi;


//    @NotNull(message = "Không được để trống hình ảnh", groups = {UpdateGroup.class, CreateGroup.class})
//    List<String> listAnhGiayTra = new ArrayList<>();
}

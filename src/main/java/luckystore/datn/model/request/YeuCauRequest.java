package luckystore.datn.model.request;

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
public class YeuCauRequest {

    private Long id;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long nguoiThucHien;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long hoaDon;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Integer loaiYeuCau;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Integer trangThai;

    private String ghiChu;

    private List<YeuCauChiTietRequest> listYeuCauChiTiet;

}

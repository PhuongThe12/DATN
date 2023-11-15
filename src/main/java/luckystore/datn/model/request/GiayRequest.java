package luckystore.datn.model.request;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import luckystore.datn.validation.groups.UpdateGroup;
import luckystore.datn.validation.groups.UpdateSoLuongGroup;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GroupSequence({UpdateGroup.class, UpdateSoLuongGroup.class, GiayRequest.class})
public class GiayRequest implements Serializable {

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, UpdateSoLuongGroup.class})
    private Long id;

    @NotNull(message = "Không được để trống hình ảnh", groups = {UpdateGroup.class, CreateGroup.class})
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    @NotNull(message = "Không được để trống tên", groups = {UpdateGroup.class, CreateGroup.class})
    @Length(message = "Tên không được vượt quá 120 ký tự", max = 120, groups = {UpdateGroup.class, CreateGroup.class})
    private String ten;

    @NotNull(message = "Không được để trống năm sản xuất", groups = {UpdateGroup.class, CreateGroup.class})
    @Min(message = "Năm sản xuất không hợp lệ", value = 999, groups = {UpdateGroup.class, CreateGroup.class})
    @Max(message = "Năm sản xuất không hợp lệ", value = 9999, groups = {UpdateGroup.class, CreateGroup.class})
    private Integer namSX;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long lotGiayId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long muiGiayId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long coGiayId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long thuongHieuId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long chatLieuId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long dayGiayId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Long deGiayId;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    private Integer trangThai = 1;

    @NotNull(message = "Không được để trống", groups = {UpdateGroup.class, CreateGroup.class})
    @Length(message = "Mô tả không được quá ngắn", min = 3, groups = {UpdateGroup.class, CreateGroup.class})
    @Length(message = "Mô tả không được quá 3000 ký tự", max = 3000, groups = {UpdateGroup.class, CreateGroup.class})
    private String moTa;

    private Set<Long> hashTagIds;

    @Valid
    private Set<BienTheGiayRequest> bienTheGiays;

    private Map<Long, String> mauSacImages;

}

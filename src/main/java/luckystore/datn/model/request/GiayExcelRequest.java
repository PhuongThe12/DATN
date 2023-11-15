package luckystore.datn.model.request;

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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiayExcelRequest implements Serializable {

    private Long id;

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 120 ký tự", max = 120)
    private String ten;

    @NotNull(message = "Không được để trống năm sản xuất", groups = {CreateGroup.class})
    @Min(message = "Năm sản xuất không hợp lệ", value = 999, groups = {UpdateGroup.class, CreateGroup.class})
    @Max(message = "Năm sản xuất không hợp lệ", value = 9999, groups = {UpdateGroup.class, CreateGroup.class})
    private Integer namSX;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String lotGiay;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String muiGiay;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String coGiay;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String thuongHieu;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String chatLieu;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String dayGiay;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private String deGiay;

    private Integer trangThai = 1;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    @Length(message = "Mô tả không được quá ngắn", min = 3, groups = {UpdateGroup.class, CreateGroup.class})
    @Length(message = "Mô tả không được quá 3000 ký tự", max = 3000, groups = {UpdateGroup.class, CreateGroup.class})
    private String moTa;

    private String hashTags;

    @Valid
    private Set<BienTheGiayExcelRequest> bienTheGiays;

    private Map<String, String> mauSacImages;

    private Integer row;

}

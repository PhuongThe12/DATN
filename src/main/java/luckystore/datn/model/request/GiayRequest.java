package luckystore.datn.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiayRequest {

    @NotNull(message = "Không được để trống hình ảnh")
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 120 ký tự", max = 120)
    private String ten;

    @NotNull(message = "Không được để trống năm sản xuất")
    @Min(message = "Năm sản xuất không hợp lệ", value = 999)
    @Max(message = "Năm sản xuất không hợp lệ", value = 9999)
    private Integer namSX;

    @NotNull(message = "Không được để trống")
    private Long lotGiayId;

    @NotNull(message = "Không được để trống")
    private Long muiGiayId;

    @NotNull(message = "Không được để trống")
    private Long coGiayId;

    @NotNull(message = "Không được để trống")
    private Long thuongHieuId;

    @NotNull(message = "Không được để trống")
    private Long chatLieuId;

    @NotNull(message = "Không được để trống")
    private Long dayGiayId;

    @NotNull(message = "Không được để trống")
    private Long deGiayId;

    @NotNull(message = "Không được để trống")
    private Integer trangThai;

    @NotNull(message = "Không được để trống")
    @Length(message = "Mô tả không được quá ngắn", min = 3)
    @Length(message = "Mô tả không được quá 3000 ký tự", max = 3000)
    private String moTa;

    private Set<Long> hashTagIds;

    @Valid
    private Set<BienTheGiayRequest> bienTheGiays;

    private Map<Long, String> mauSacImages;

}

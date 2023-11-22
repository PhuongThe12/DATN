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

    private String ten;

    private Integer namSX;

    private String lotGiay;

    private String muiGiay;

    private String coGiay;

    private String thuongHieu;

    private String chatLieu;

    private String dayGiay;

    private String deGiay;

    private Integer trangThai = 1;

    private String moTa;

    private Set<String> hashTags;

    private Set<BienTheGiayExcelRequest> bienTheGiays;

    private Map<String, String> mauSacImages;

    private Integer row;

}

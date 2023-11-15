package luckystore.datn.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.validation.groups.CreateGroup;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BienTheGiayExcelRequest implements Serializable {

    private Long id;

    @NotNull(message = "Không được để trống")
    private String mauSac;

    @NotNull(message = "Không được để trống")
    private String kichThuoc;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    @DecimalMin(message = "Không được âm", value = "0.0")
    private BigDecimal giaBan;

    //    @NotNull(message = "Không được để trống")
    private Integer trangThai = 1;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    @Length(message = "Không được vượt quá 20 ký tự", max = 20)
    private String barcode;

    @NotNull(message = "Không được để trống", groups = {CreateGroup.class})
    private Integer soLuong;

    private Integer column;

}

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

    private String mauSac;

    private String kichThuoc;

    private BigDecimal giaBan;

    private Integer trangThai = 1;

    private String barcode;

    private Integer soLuong;

    private Integer column;

    private Integer row;

}

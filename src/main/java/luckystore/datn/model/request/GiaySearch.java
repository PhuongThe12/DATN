package luckystore.datn.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiaySearch {

    private String ten;

    private Integer namSX;

    private BigDecimal giaTu;

    private BigDecimal giaDen;

    private String tenThuongHieu;

    private Integer currentPage = 1;

    private Integer pageSize = 5;

    private Set<Long> thuongHieuIds;

    private Set<Long> kichThuocIds;

    private Set<Long> mauSacIds;

    private Integer trangThai;

    private Integer lastDate;

}

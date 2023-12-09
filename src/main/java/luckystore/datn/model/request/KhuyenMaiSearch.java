package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiSearch {


    private Long id;

    private List<Long> bienTheIds;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private String ten;

    private int status = 1;

    private int currentPage = 1;

    private int pageSize = 5;
}

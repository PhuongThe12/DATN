package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonSearchP {

    private Long id;

    private LocalDateTime tuNgay;

    private LocalDateTime denNgay;

    private int trangThai;

    private Integer currentPage = 1;

    private Integer pageSize = 5;

}

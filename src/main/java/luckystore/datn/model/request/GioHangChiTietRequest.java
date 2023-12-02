package luckystore.datn.model.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GioHangChiTietRequest {

    private Long id;

    private Long gioHang;

    private Long bienTheGiay;

    private Integer soLuong;

    private BigDecimal giaBan;

    private LocalDateTime ngayTao;

    private String ghiChu;
}

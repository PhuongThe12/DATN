package luckystore.datn.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMaiChiTietRequest {

    private Long id;

    private Long bienTheGiayId;

    private Integer phanTramGiam;

}

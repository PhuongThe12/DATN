package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderProcuctRequest {

    private Long idHoaDon;

    private Long idGiay;

    private Integer soLuong;

}

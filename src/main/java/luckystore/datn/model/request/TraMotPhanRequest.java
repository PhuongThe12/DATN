package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraMotPhanRequest {

    private Long idHoaDon;
    private List<HoaDonChiTietRequest> chiTietHoaDons = new ArrayList<>();

}

package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.KhuyenMaiChiTiet;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiChiTietGiayResponse {

    private List<GiayResponse> giays;

}

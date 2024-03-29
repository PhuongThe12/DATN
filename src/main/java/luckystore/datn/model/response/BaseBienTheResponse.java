package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@SuperBuilder
@Data
public class BaseBienTheResponse {

    private Long idKhuyenMaiChiTiet;
    private Integer phanTramGiam;

    public BaseBienTheResponse(Integer phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }
}

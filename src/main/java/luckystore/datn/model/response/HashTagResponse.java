package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HashTag;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTagResponse {

    private Long id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    public HashTagResponse(HashTag hashTag) {
        if (hashTag != null) {
            this.id = hashTag.getId();
            this.ten = hashTag.getTen();
            this.moTa = hashTag.getMoTa();
            this.trangThai = hashTag.getTrangThai();
        }
    }

}

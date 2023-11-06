package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HashTagChiTiet;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTagChiTietResponse {

    private Long id;

    private HashTagResponse hashTag;

    public HashTagChiTietResponse(HashTagChiTiet hashTagChiTiet) {
        if(hashTagChiTiet != null) {
            this.id = hashTagChiTiet.getId();
            this.hashTag = new HashTagResponse(hashTagChiTiet.getHashTag());
        }
    }

}

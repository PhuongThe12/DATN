package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Districts;
import luckystore.datn.entity.Provinces;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictResponse {
    private String id;

    private String ten;

    private String nameEn;

    private String fullName;

    private String fullNameEn;

    private String codeName;

    private Provinces provinces;

    public DistrictResponse(Districts districts) {
        if (districts != null) {
            this.id = districts.getId();
            this.ten = districts.getTen();
            this.nameEn = districts.getNameEn();
            this.fullName = districts.getFullName();
            this.fullNameEn = districts.getFullNameEn();
            this.codeName = districts.getCodeName();

        }
    }
}

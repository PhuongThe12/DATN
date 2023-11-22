package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Districts;
import luckystore.datn.entity.Wards;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WardResponse {

    private String id;

    private String ten;

    private String nameEn;

    private String fullName;

    private String fullNameEn;

    private String codeName;

    private Districts districts;

    public WardResponse(Wards wards) {
        if (wards != null) {
            this.id = wards.getId();
            this.ten = wards.getTen();
            this.nameEn = wards.getNameEn();
            this.fullName = wards.getFullName();
            this.fullNameEn = wards.getFullNameEn();
            this.codeName = wards.getCodeName();
            this.districts = wards.getDistricts();

        }
    }
}

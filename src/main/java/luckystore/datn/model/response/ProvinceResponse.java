package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.Provinces;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceResponse {

    private String id;

    private String ten;

    private String nameEn;

    private String fullName;

    private String fullNameEn;

    private String codeName;

    public ProvinceResponse(Provinces provinces) {
        if (provinces != null) {
            this.id = provinces.getId();
            this.ten = provinces.getTen();
            this.nameEn = provinces.getNameEn();
            this.fullName = provinces.getFullName();
            this.fullNameEn = provinces.getFullNameEn();
            this.codeName = provinces.getCodeName();
        }
    }
}

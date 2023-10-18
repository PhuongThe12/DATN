package luckystore.datn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.MauSac;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MauSacDto {

    private Long id;

    @NotNull(message = "Không được để trống tên")
    @Length(message = "Tên không được vượt quá 50 ký tự")
    private String ten;

    private String moTa;

    public MauSacDto(MauSac mauSac) {
        if(mauSac != null) {
            this.id = mauSac.getId();
            this.ten = mauSac.getTen();
            this.moTa = mauSac.getMoTa();
        }
    }

}

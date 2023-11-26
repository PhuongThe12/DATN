package luckystore.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HoaDonBanHangResponse {

    private Long id;

    private List<HoaDonChiTietResponse> hoaDonChiTiets = new ArrayList<>();

    private Integer trangThai;

    private KhachHangResponse khachHangRestponse;

    public HoaDonBanHangResponse(Long id, HoaDonChiTiet hoaDonChiTiet, Integer trangThai) {
        this.id = id;
        if (hoaDonChiTiet != null) {
            this.hoaDonChiTiets.add(new HoaDonChiTietResponse(hoaDonChiTiet));
        }
        this.trangThai = trangThai;
    }

    public HoaDonBanHangResponse(HoaDon hoaDon, Integer trangThai) {
        this.id = hoaDon.getId();
        this.trangThai = trangThai;
    }

    public HoaDonBanHangResponse(Long id) {
        this.id = id;
    }
}

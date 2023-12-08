package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChiTietKhuyenMaiResponse {

    private Long id;

    private String ten;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private KhuyenMaiChiTietGiayResponse khuyenMaiChiTietResponses;

    public ChiTietKhuyenMaiResponse(KhuyenMaiResponse khuyenMai) {
        this.id = khuyenMai.getId();
        this.ten = khuyenMai.getTen();
        this.ngayBatDau = khuyenMai.getNgayBatDau();
        this.ngayKetThuc = khuyenMai.getNgayKetThuc();
        this.ghiChu = khuyenMai.getGhiChu();
        this.trangThai = khuyenMai.getTrangThai();
        var chiTietResponses = khuyenMai.getKhuyenMaiChiTietResponses();
        Map<Long, GiayResponse> giays = new HashMap<Long, GiayResponse>();
        KhuyenMaiChiTietGiayResponse aaa = new KhuyenMaiChiTietGiayResponse();
        for (var chiTietResponse : chiTietResponses) {
            var bietTheGiay = chiTietResponse.getBienTheGiayResponsel();
            bietTheGiay.setPhanTramGiam(chiTietResponse.getPhanTramGiam());
            bietTheGiay.setIdKhuyenMaiChiTiet(chiTietResponse.getId());
            var giay = bietTheGiay.getGiayResponse();
            bietTheGiay.setGiayResponse(null);
            if (!giays.containsKey(giay.getId())) {
                giays.put(giay.getId(), giay);
            }
            if (giays.get(giay.getId()).getLstBienTheGiay() == null) {
                giays.get(giay.getId()).setLstBienTheGiay(List.of(bietTheGiay));
            } else {
                var list = new java.util.ArrayList<>(giays.get(giay.getId()).getLstBienTheGiay().stream().toList());
                list.add(bietTheGiay);
                giays.get(giay.getId()).setLstBienTheGiay(list);
            }
        }
        aaa.setGiays(giays.values().stream().toList());
        this.khuyenMaiChiTietResponses = aaa;

    }

}

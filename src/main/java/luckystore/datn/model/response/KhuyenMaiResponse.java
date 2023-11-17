package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.entity.KhuyenMaiChiTiet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiResponse {

    private Long id;

    private String ten;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private List<KhuyenMaiChiTietResponse> khuyenMaiChiTietResponses;

    public KhuyenMaiResponse(KhuyenMai khuyenMai){
        this.id = khuyenMai.getId();
        this.ten = khuyenMai.getTen();
        this.ngayBatDau = khuyenMai.getNgayBatDau();
        this.ngayKetThuc = khuyenMai.getNgayKetThuc();
        this.ghiChu = khuyenMai.getGhiChu();
        this.trangThai = khuyenMai.getTrangThai();
        List<KhuyenMaiChiTiet> khuyenMaiChiTiets = khuyenMai.getKhuyenMaiChiTiets();
        this.khuyenMaiChiTietResponses = khuyenMaiChiTiets.stream().map(KhuyenMaiChiTietResponse::new).collect(Collectors.toList());
    }

}

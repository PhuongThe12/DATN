package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.entity.KhuyenMaiChiTiet;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiResponse {

    private Long id;

    private String ten;

    private String ngayBatDau;

    private String ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private List<KhuyenMaiChiTietResponse> khuyenMaiChiTietResponses;

    public KhuyenMaiResponse(KhuyenMai khuyenMai) {
        this.id = khuyenMai.getId();
        this.ten = khuyenMai.getTen();
        this.ngayBatDau = dateFormat(khuyenMai.getNgayBatDau());
        this.ngayKetThuc = dateFormat(khuyenMai.getNgayKetThuc());
        this.ghiChu = khuyenMai.getGhiChu();
        this.trangThai = khuyenMai.getTrangThai();
        List<KhuyenMaiChiTiet> khuyenMaiChiTiets = khuyenMai.getKhuyenMaiChiTiets();
        this.khuyenMaiChiTietResponses = khuyenMaiChiTiets.stream().map(KhuyenMaiChiTietResponse::new).collect(Collectors.toList());
    }

    private String dateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }

}

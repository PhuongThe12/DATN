package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.DotGiamGia;
import luckystore.datn.model.request.DieuKienRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DotGiamGiaResponse {

    private Long id;

    private String ten;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private List<DieuKienResponse> dieuKienResponses;

    public DotGiamGiaResponse(DotGiamGia dotGiamGia) {
        this.id = dotGiamGia.getId();
        this.ten = dotGiamGia.getTen();
        this.ngayBatDau = dotGiamGia.getNgayBatDau();
        this.ngayKetThuc = dotGiamGia.getNgayKetThuc();
        this.ghiChu = dotGiamGia.getGhiChu();
        this.trangThai = dotGiamGia.getTrangThai();
        List<DieuKien> dieuKiens = dotGiamGia.getDanhSachDieuKien();
        this.dieuKienResponses = dieuKiens.stream().map(DieuKienResponse::new).collect(Collectors.toList());
    }

}


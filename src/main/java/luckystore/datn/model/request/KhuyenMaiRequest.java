package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiRequest {

    private Long id;

    private String ten;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private List<KhuyenMaiChiTietRequest> khuyenMaiChiTietRequests;

}

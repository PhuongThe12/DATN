package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YeuCauSearch {

    private Long idYeuCau;

    private String idNhanVien;

    private String tenKhachHang;

    private String soDienThoaiKhachHang;

    private Integer trangThai;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private String email;

    private Integer currentPage = 1;

    private Integer pageSize = 5;

}

package luckystore.datn.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhuyenMaiRequest {

    private Long id;

    @NotNull(message = "Tên không được trống")
    @Length(max = 50, message = "Tên không được quá 50 ký tự")
    private String ten;

    @NotNull(message = "Ngày bắt đầu không được trống")
    private LocalDateTime ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được trống")
    private LocalDateTime ngayKetThuc;

    @NotNull(message = "Không được trống ghi chú")
    private String ghiChu;

    @NotNull(message = "Không được trống trạng thái")
    private Integer trangThai;

    @Valid
    private List<KhuyenMaiChiTietRequest> khuyenMaiChiTietRequests;

}

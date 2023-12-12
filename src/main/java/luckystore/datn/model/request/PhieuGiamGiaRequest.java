package luckystore.datn.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PhieuGiamGiaRequest {

    @NotBlank(message = "Nhập mã giảm giá")
    @Length(message = "Độ dài mã trong khoảng 5- 10 ký tự", min = 5)
    @Length(message = "Độ dài mã trong khoảng 5- 10 ký tự", max = 10)
    private String maGiamGia;

    @NotNull(message = "Nhập phần trăm giảm")
    @Min(value = 0, message = "Không được âm")
    private Integer phanTramGiam;

    @NotNull(message = "Nhập số lượng phiếu")
    @Min(value = 0, message = "Không được âm")
    private Integer soLuongPhieu;

    @NotNull(message = "Nhập ngày bắt đầu")
    private Long ngayBatDau;

    @NotNull(message = "Nhập ngày kết thúc")
    private Long ngayKetThuc;

    @NotNull(message = "Nhập giá trị đơn hàng tối thiểu")
    @Min(value = 0, message = "Không được âm")
    private BigDecimal giaTriDonToiThieu;

    @NotNull(message = "Nhập giá trị giảm tối đa")
    @Min(value = 0, message = "Không được âm")
    private BigDecimal giaTriGiamToiDa;

    @NotNull(message = "Chọn hạng khách hàng ")
    private String hangKhachHang;

    private Long nhanVienId;

    private LocalDateTime ngayTao;

    private Integer trangThai;


}

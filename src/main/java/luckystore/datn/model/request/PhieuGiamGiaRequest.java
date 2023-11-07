package luckystore.datn.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhieuGiamGiaRequest {

    private Long id;

    @NotNull(message = "Không được để trống mã!")
    private String maGiamGia;

    @NotNull(message = "Không được để trống!")
    private int phanTramGiam;

    @NotNull(message = "Số lượng được để trống!")
    private int soLuongPhieu;

    @NotNull(message = "Không được để trống ngày bắt đầu!")
//    @Pattern(regexp = "#(^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$)", message = "Không đúng dịnh dạng!")
    private LocalDateTime ngayBatDau;

    @NotNull(message = "Không được để trống ngày bắt đầu")
//    @Pattern(regexp = "#(^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$)", message = "Không đúng dịnh dạng!")
    private LocalDateTime ngayKetThuc;

    @NotNull(message = "Không được để trống giá trị đơn tối thiểu!")
    private BigDecimal giaTriDonToiThieu;

    @NotNull(message = "Không được để trống giá trị giảm tối đa!")
    private BigDecimal giaTriGiamToiDa;

    private String nguoiTao;

    private String doiTuongApDung;

    private int trangThai;

}

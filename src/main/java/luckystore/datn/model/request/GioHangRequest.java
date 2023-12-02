package luckystore.datn.model.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class GioHangRequest {

    public Long id;

    public KhachHangRequest khachHang;

    public LocalDateTime ngayTao;

    public String ghiChu;

    public Integer trangThai;

    List<GioHangChiTietRequest> gioHangChiTietRequestList;
}

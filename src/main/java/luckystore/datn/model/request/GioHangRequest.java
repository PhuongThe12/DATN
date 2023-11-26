package luckystore.datn.model.request;

import java.time.LocalDateTime;
import java.util.List;

public class GioHangRequest {

    private Long id;

    private Long khachHang;

    private LocalDateTime ngayTao;

    private String ghiChu;

    private Integer trangThai;

    List<GioHangChiTietRequest> gioHangChiTietRequestList;
}

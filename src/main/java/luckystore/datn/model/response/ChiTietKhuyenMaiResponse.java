package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChiTietKhuyenMaiResponse {

    private Long id;

    private String ten;

    private String ngayBatDau;

    private String ngayKetThuc;

    private String ghiChu;

    private Integer trangThai;

    private KhuyenMaiChiTietGiayResponse khuyenMaiChiTietResponses;

    public ChiTietKhuyenMaiResponse(Long id, String ten, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai, String ghiChu) {
        this.id = id;
        this.ten = ten;
        this.ngayBatDau = dateFormat(ngayBatDau);
        this.ngayKetThuc = dateFormat(ngayKetThuc);
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }


    private String dateFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

}

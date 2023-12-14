package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanhGiaRequest {

    private Integer saoDanhGia;

    private String binhLuan;

//    private Integer trangThai;
//
//    private Date thoiGian;
//
//    private Date ngayTao;

    private Long idKhachHang;

    private Long idGiay;

}

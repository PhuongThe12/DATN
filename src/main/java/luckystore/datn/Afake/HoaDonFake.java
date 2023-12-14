package luckystore.datn.Afake;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HoaDonFake {

    private int ngay;

    private int thang;

    private int nam;

    private int soLuongHoaDon;

    private int trangThai;

    private List<Long> idsGiay;

    private Long idNhanVien;

    private Long idKhachHang;
}

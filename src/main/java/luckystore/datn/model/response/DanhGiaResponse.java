package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.DanhGia;
import luckystore.datn.entity.KhachHang;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DanhGiaResponse {

    private Long id;

    private Integer saoDanhGia;

    private String binhLuan;

    private Integer trangThai;

    private Date thoiGian;

    private Date ngayTao;

    private GiayResponse giayResponse;

    private KhachHangResponse khachHangResponse;

    public DanhGiaResponse(DanhGia danhGia) {
        if (danhGia != null) {
            this.id = danhGia.getId();
            this.saoDanhGia = danhGia.getSaoDanhGia();
            this.binhLuan = danhGia.getBinhLuan();
            this.trangThai = danhGia.getTrangThai();
            this.thoiGian = danhGia.getThoiGian();
            this.ngayTao = danhGia.getNgayTao();
            this.giayResponse = new GiayResponse(danhGia.getGiay());
            this.khachHangResponse = new KhachHangResponse(danhGia.getKhachHang());
        }
    }

    public DanhGiaResponse(Long id, Integer saoDanhGia, String binhLuan, Integer trangThai, Date thoiGian,
                           Date ngayTao, KhachHang khachHang) {
        this.id = id;
        this.saoDanhGia = saoDanhGia;
        this.binhLuan = binhLuan;
        this.trangThai = trangThai;
        this.thoiGian = thoiGian;
        this.ngayTao = ngayTao;
        this.khachHangResponse = new KhachHangResponse(khachHang);
    }

    public DanhGiaResponse(Long id, Integer saoDanhGia, String binhLuan, Integer trangThai, Date thoiGian,
                           Date ngayTao, Long idGiay, String ten, String thumbnail, KhachHang khachHang) {
        this.id = id;
        this.saoDanhGia = saoDanhGia;
        this.binhLuan = binhLuan;
        this.trangThai = trangThai;
        this.thoiGian = thoiGian;
        this.ngayTao = ngayTao;
        this.giayResponse = new GiayResponse(idGiay, ten, thumbnail);
        this.khachHangResponse = new KhachHangResponse(khachHang);
    }

}

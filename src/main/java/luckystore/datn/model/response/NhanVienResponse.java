package luckystore.datn.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.entity.TaiKhoan;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NhanVienResponse {
    private Long id;

    private String hoTen;

    private Integer gioiTinh;

    private Date ngaySinh;

    private String soDienThoai;

    private String email;

    private String ghiChu;

    private Integer trangThai;

    private String xa;

    private String huyen;

    private String tinh;

    private Integer chucVu;

    private TaiKhoan taiKhoan;

    public NhanVienResponse(NhanVien nhanVien) {
        if (nhanVien != null) {
            this.id = nhanVien.getId();
            this.hoTen = nhanVien.getHoTen();
            this.gioiTinh = nhanVien.getGioiTinh();
            this.ngaySinh = nhanVien.getNgaySinh();
            this.soDienThoai = nhanVien.getSoDienThoai();
            this.email = nhanVien.getEmail();
            this.ghiChu = nhanVien.getGhiChu();
            this.trangThai = nhanVien.getTrangThai();
            this.xa = nhanVien.getXa();
            this.huyen = nhanVien.getHuyen();
            this.tinh = nhanVien.getTinh();
            this.chucVu = nhanVien.getChucVu();
            this.taiKhoan = nhanVien.getTaiKhoan();
        }
    }

    public NhanVienResponse(Long id, String ten) {
        this.id = id;
        this.hoTen = ten;
    }
}

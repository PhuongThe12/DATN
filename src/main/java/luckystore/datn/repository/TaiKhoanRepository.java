package luckystore.datn.repository;

import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.model.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan,Long> {
    Boolean existsByTenDangNhap(String tenDangNhap);

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);

    @Query(value = "SELECT TOP 1 * FROM TaiKhoan WHERE TEN_DANG_NHAP = ?1 AND MAT_KHAU = ?2" , nativeQuery = true)
    TaiKhoan findByTenDangNhapAndMatKhau(String tenDangNhap , String matKhau);

//    Boolean existsByHoTenAndIdNot(String ten, Long id);
//    cuong them tai khoan khach hang
//    Optional<KhachHang> findByTenDangNhap(String email);
    @Query("select new luckystore.datn.model.response.TaiKhoanResponse(tk) from TaiKhoan tk")
    List<TaiKhoanResponse> findAllResponse();

    @Query("select tk from TaiKhoan tk where tk.tenDangNhap = :email")
    Optional<TaiKhoan> findByEmail(String email);
}

package luckystore.datn.repository;

import luckystore.datn.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan,Long> {
    Boolean existsByTenDangNhap(String tenDangNhap);

    @Query(value = "SELECT TOP 1 * FROM TaiKhoan WHERE TEN_DANG_NHAP = ?1 AND MAT_KHAU = ?2" , nativeQuery = true)
    TaiKhoan findByTenDangNhapAndMatKhau(String tenDangNhap , String matKhau);

//    Boolean existsByHoTenAndIdNot(String ten, Long id);

}

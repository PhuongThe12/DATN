package luckystore.datn.repository;

import luckystore.datn.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan,Long> {
    Boolean existsByTenDangNhap(String ten);

//    Boolean existsByHoTenAndIdNot(String ten, Long id);

}

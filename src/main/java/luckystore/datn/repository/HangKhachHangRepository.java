package luckystore.datn.repository;

import luckystore.datn.entity.HangKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HangKhachHangRepository extends JpaRepository<HangKhachHang, Long> {

    boolean existsHangKhachHangByTenHang(String tenHang);

    boolean existsHangKhachHangById(Long id);

    HangKhachHang findHangKhachHangByTenHang(String ten);
}

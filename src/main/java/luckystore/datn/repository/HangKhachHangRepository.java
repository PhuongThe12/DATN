package luckystore.datn.repository;

import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.model.response.HangKhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HangKhachHangRepository extends JpaRepository<HangKhachHang,Long> {
    @Query("select new luckystore.datn.model.response.HangKhachHangResponse(hkh) from HangKhachHang hkh")
    List<HangKhachHangResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HangKhachHangResponse(hkh) from HangKhachHang hkh " +
            "WHERE (:searchText IS NULL OR hkh.tenHang LIKE %:searchText%) AND (:status IS NULL OR hkh.trangThai = :status)")
    Page<HangKhachHangResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTenHang(String ten);

    Boolean existsByTenHangAndIdNot(String ten, Long id);
}
